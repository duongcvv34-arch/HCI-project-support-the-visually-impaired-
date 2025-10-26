package com.example.newvoice

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var rgGender: RadioGroup
    private lateinit var rgLanguage: RadioGroup
    private lateinit var seekBarSpeed: SeekBar
    private lateinit var tvSpeedValue: TextView
    private lateinit var btnApply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        rgGender = findViewById(R.id.rgGender)
        rgLanguage = findViewById(R.id.rgLanguage)
        seekBarSpeed = findViewById(R.id.seekBarSpeed)
        tvSpeedValue = findViewById(R.id.tvSpeedValue)
        btnApply = findViewById(R.id.btnApply)

        // Cập nhật hiển thị tốc độ
        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val speed = 0.5f + (progress / 100f * 1.5f) // map 0..150 -> 0.5..2.0
                tvSpeedValue.text = String.format("%.1fx", speed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnApply.setOnClickListener {
            val gender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "Nam" else "Nữ"
            val language = if (rgLanguage.checkedRadioButtonId == R.id.rbEnglish) "Tiếng Anh" else "Tiếng Việt"
            val speed = tvSpeedValue.text.toString()

            Toast.makeText(
                this,
                "Đã áp dụng: $gender - $language - tốc độ $speed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
