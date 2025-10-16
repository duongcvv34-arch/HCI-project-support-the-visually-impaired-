package com.example.newvoice

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.graphics.PixelFormat
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.Locale

class FocusReadService : AccessibilityService(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var overlay: HighlightOverlay? = null
    private lateinit var windowManager: WindowManager
    private val handler = Handler(Looper.getMainLooper())
    private var lastText: String? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("FocusReadService", "Accessibility Service connected")

        tts = TextToSpeech(this, this)

        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
        serviceInfo = info

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        overlay = HighlightOverlay(this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            ttsReady = true
            tts?.setLanguage(Locale("vi", "VN"))
            tts?.setSpeechRate(1.3f) // 🔹 Tốc độ đọc nhanh hơn
            tts?.setPitch(1.05f)     // 🔹 Giữ giọng tự nhiên, không méo
            Log.d("FocusReadService", "TTS initialized (Fast mode)")
        } else {
            Log.e("FocusReadService", "TTS initialization failed")
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!ttsReady || event == null) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_FOCUSED,
            AccessibilityEvent.TYPE_VIEW_CLICKED,
            AccessibilityEvent.TYPE_VIEW_SELECTED,
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> {

                val node = event.source ?: return
                val text = getAllText(node)

                if (text.isNotBlank() && text != lastText) {
                    showHighlight(node)
                    playFocusSound() // phát âm thanh nhẹ
                    speakImmediately(text)
                    lastText = text
                }
            }
        }
    }

    /** Phát âm thanh focus nhẹ song song với TTS **/
    private fun playFocusSound() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, R.raw.focus_music).apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                start()
            }
        } catch (e: Exception) {
            Log.e("FocusReadService", "Lỗi phát âm thanh: ${e.message}")
        }
    }

    /** Đọc ngay lập tức **/
    private fun speakImmediately(text: String) {
        try {
            tts?.stop()
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "focus_read")
            Log.d("FocusReadService", "Đọc nhanh: $text")
        } catch (e: Exception) {
            Log.e("FocusReadService", "Lỗi đọc văn bản: ${e.message}")
        }
    }

    /** Hiển thị highlight nhanh **/
    private fun showHighlight(node: AccessibilityNodeInfo) {
        try {
            val bounds = Rect()
            node.getBoundsInScreen(bounds)
            handler.post {
                overlay?.highlight(bounds)

                val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT
                )
                params.gravity = Gravity.TOP or Gravity.START

                if (overlay?.windowToken == null) {
                    windowManager.addView(overlay, params)
                }

                handler.postDelayed({ overlay?.clear() }, 700)
            }
        } catch (e: Exception) {
            Log.e("FocusReadService", "Lỗi highlight: ${e.message}")
        }
    }

    private fun getAllText(node: AccessibilityNodeInfo?): String {
        if (node == null) return ""
        val builder = StringBuilder()

        node.text?.let {
            if (it.isNotBlank()) builder.append(it).append(". ")
        }
        node.contentDescription?.let {
            if (it.isNotBlank()) builder.append(it).append(". ")
        }

        for (i in 0 until node.childCount) {
            builder.append(getAllText(node.getChild(i)))
        }

        return builder.toString().trim()
    }

    override fun onInterrupt() {
        tts?.stop()
    }

    override fun onDestroy() {
        try {
            mediaPlayer?.release()
            tts?.stop()
            tts?.shutdown()
            overlay?.let { windowManager.removeViewImmediate(it) }
        } catch (_: Exception) {}
        super.onDestroy()
    }
}
