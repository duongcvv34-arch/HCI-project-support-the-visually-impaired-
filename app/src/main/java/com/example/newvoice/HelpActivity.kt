package com.example.newvoice

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newvoice.R  // <-- Import R của project, KHÔNG import android.R
import java.util.*

class HelpActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var helpTextView: TextView

    private val helpText = """Xin chào! Sau đây là một số thông tin về các chức năng của ứng dụng giúp bạn hiểu rõ hơn và hỗ trợ trong quá trình sử dụng:

1. ĐỌC MÀN HÌNH
    - Giúp bạn nghe được nội dung trên màn hình.
    - Nhấn nút “Đọc màn hình để bật chức năng”.
    - Bấm “Dừng” để tắt tính năng.
        
2. RA LỆNH BẰNG GIỌNG NÓI
    - Giúp bạn điều khiển ứng dụng dễ dàng.
    - Các lệnh phổ biến: “Mở đọc màn hình”, “Tăng âm lượng”, “Giảm âm lượng”, “Đọc lại”, “Trợ giúp”.

3. CÀI ĐẶT
   - Giúp Cá nhân hóa trải nghiệm nghe sao cho phù hợp.
   - Giọng đọc: Chọn giọng nam hoặc nữ.
   - Tốc độ đọc: Chọn nhanh hoặc chậm.
    - Âm lượng: Tăng hoặc giảm.
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)  // <-- đảm bảo file layout tồn tại

        helpTextView = findViewById(R.id.helpTextView)
        helpTextView.text = helpText

        // Khởi tạo Text-to-Speech
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("vi", "VN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                println("Ngôn ngữ không được hỗ trợ")
            } else {
                tts.speak(helpText, TextToSpeech.QUEUE_FLUSH, null, "HELP_TTS")
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
