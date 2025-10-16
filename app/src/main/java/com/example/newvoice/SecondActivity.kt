package com.example.newvoice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second) // liên kết với file XML

        // Khai báo các nút
        val buttonRead: Button = findViewById(R.id.button)      // "Đọc văn bản"
        val buttonNavigate: Button = findViewById(R.id.button2) // "Điều hướng"
        val buttonSettings: Button = findViewById(R.id.button3) // "Cài đặt"
        val buttonHelp: Button = findViewById(R.id.button5)     // "Trợ giúp"

        // Xử lý sự kiện click
        buttonRead.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Đọc văn bản", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ReadTextActivity::class.java)
            startActivity(intent)
        }

        buttonNavigate.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Điều hướng", Toast.LENGTH_SHORT).show()

        }

        buttonSettings.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Cài đặt", Toast.LENGTH_SHORT).show()

        }

        buttonHelp.setOnClickListener {
            Toast.makeText(this, "Bạn chọn: Trợ giúp", Toast.LENGTH_SHORT).show()

        }
    }
}
