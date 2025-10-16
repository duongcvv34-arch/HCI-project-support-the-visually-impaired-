package com.example.newvoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ReadTextActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private SharedPreferences prefs;
    private boolean ttsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_text);

        Button buttonActivate = findViewById(R.id.buttonActivateTTS);
        Button buttonDeactivate = findViewById(R.id.buttonDeactivateTTS);

        prefs = getSharedPreferences("TTS_PREFS", MODE_PRIVATE);

        // 🚀 Khởi tạo TTS
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.forLanguageTag("vi-VN"));
                tts.setPitch(1.0f);
                tts.setSpeechRate(0.9f);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Thiết bị chưa hỗ trợ tiếng Việt. Đang mở cài đặt để tải giọng nói...", Toast.LENGTH_LONG).show();
                    Intent installIntent = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                } else {
                    ttsReady = true;
                    speakText("Trình đọc văn bản đã sẵn sàng.");
                }
            } else {
                Toast.makeText(this, "Không thể khởi tạo TTS", Toast.LENGTH_SHORT).show();
            }
        });

        // 🟢 Nút BẬT TÍNH NĂNG
        buttonActivate.setOnClickListener(new DoubleTapListener() {
            @Override
            public void onSingleTap(android.view.View v) {
                Log.d("TTS", "Single tap Activate");
                if (ttsReady) speakText("Nhấn hai lần để bật tính năng đọc văn bản.");
            }

            @Override
            public void onDoubleTap(android.view.View v) {
                Log.d("TTS", "Double tap Activate");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("TTS_ENABLED", true);
                editor.putBoolean("READ_ALL_ENABLED", false);
                editor.putString("TTS_LANGUAGE", "Vietnamese");
                editor.apply();

                if (ttsReady)
                    speakText("Đã bật tính năng đọc văn bản. Vui lòng bật quyền trợ năng cho ứng dụng.");

                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        // 🔴 Nút TẮT TÍNH NĂNG
        buttonDeactivate.setOnClickListener(new DoubleTapListener() {
            @Override
            public void onSingleTap(android.view.View v) {
                Log.d("TTS", "Single tap Deactivate");
                if (ttsReady) speakText("Nhấn hai lần để tắt tính năng đọc văn bản.");
            }

            @Override
            public void onDoubleTap(android.view.View v) {
                Log.d("TTS", "Double tap Deactivate");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("TTS_ENABLED", false);
                editor.putBoolean("READ_ALL_ENABLED", false);
                editor.apply();

                if (ttsReady) speakText("Đã tắt tính năng đọc văn bản.");
                Toast.makeText(ReadTextActivity.this, "Đã tắt TTS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void speakText(String text) {
        if (tts == null || !ttsReady) return;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_id");
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
