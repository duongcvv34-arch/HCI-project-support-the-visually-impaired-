package com.example.newvoice

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second) // Liên kết với file XML

        // --- Khai báo các nút ---
        val buttonRead: LinearLayout = findViewById(R.id.btnReadText)     // "Đọc văn bản"
        val buttonNavigate: LinearLayout = findViewById(R.id.btnNavigate) // "Điều hướng"
        val buttonSettings: LinearLayout = findViewById(R.id.btnSettings) // "Cài đặt"
        val buttonHelp: LinearLayout = findViewById(R.id.btnHelp)         // "Trợ giúp"

        // --- Xử lý sự kiện khi nhấn ---
        buttonRead.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Đọc văn bản", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ReadTextActivity::class.java)
            startActivity(intent)
        }

        buttonNavigate.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Ra lệnh", Toast.LENGTH_SHORT).show()

        }

        buttonSettings.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Cài đặt", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        buttonHelp.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Trợ giúp", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)

        }
    }
}
