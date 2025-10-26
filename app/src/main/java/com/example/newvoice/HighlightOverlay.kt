package com.example.newvoice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class HighlightOverlay(context: Context) : View(context) {
    private val paint = Paint().apply {
        color = Color.parseColor("#3399FF") // Xanh nước biển
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
    }

    private var rect: Rect? = null

    fun highlight(bounds: Rect) {
        rect = bounds
        invalidate() // vẽ khung mới
    }

    // Không cần clear(), hoặc chỉ gọi khi mất focus hoàn toàn
    fun clear() {
        rect = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        rect?.let {
            canvas.drawRect(it, paint)
        }
    }
}
