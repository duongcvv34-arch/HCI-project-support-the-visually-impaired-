package com.example.newvoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Set;

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

        // Khởi tạo TTS với engine Google
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.forLanguageTag("vi-VN"));
                tts.setPitch(1.0f);
                tts.setSpeechRate(0.9f);

                // Chọn giọng
                try {
                    Set<Voice> voices = tts.getVoices();
                    if (voices != null) {
                        for (Voice voice : voices) {
                            if (voice.getLocale().equals(Locale.forLanguageTag("vi-VN"))
                                    && !voice.isNetworkConnectionRequired()
                                    && voice.getName().toLowerCase().contains("female")) {
                                tts.setVoice(voice);
                                Log.d("TTS", "Đã chọn giọng: " + voice.getName());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.w("TTS", "Không thể chọn giọng nữ: " + e.getMessage());
                }

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Thiết bị chưa hỗ trợ tiếng Việt. Đang mở cài đặt để tải giọng nói...", Toast.LENGTH_LONG).show();
                    Intent installIntent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                } else {
                    ttsReady = true;
                    speakText("Xin vui lòng chọn một trong hai nút bên dưới.");
                }
            } else {
                Toast.makeText(this, "Không thể khởi tạo Text-to-Speech", Toast.LENGTH_SHORT).show();
            }
        }, "com.google.android.tts"); // ép dùng engine Google

        //  Nút bật tính năng
        buttonActivate.setOnClickListener(new DoubleTapListener() {
            @Override
            public void onSingleTap(android.view.View v) {
                if (ttsReady) speakText("Nhấn hai lần để bật chế độ đọc màn hình.");
            }

            @Override
            public void onDoubleTap(android.view.View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("TTS_ENABLED", true);
                editor.putBoolean("READ_ALL_ENABLED", false);
                editor.putString("TTS_LANGUAGE", "Vietnamese");
                editor.apply();

                if (ttsReady)
                    speakText("Vui lòng kích hoạt tính năng cho ứng dụng trong phần cài đặt.");

                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });

        //  Nút tắt tính năng
        buttonDeactivate.setOnClickListener(new DoubleTapListener() {
            @Override
            public void onSingleTap(android.view.View v) {
                if (ttsReady) speakText("Nhấn hai lần để tắt tính năng đọc màn hình.");
            }

            @Override
            public void onDoubleTap(android.view.View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("TTS_ENABLED", false);
                editor.putBoolean("READ_ALL_ENABLED", false);
                editor.apply();

                if (ttsReady) speakText("Đã tắt tính năng đọc màn hình.");
                Toast.makeText(ReadTextActivity.this, "Đã tắt TTS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm đọc an toàn
    private void speakText(String text) {
        if (tts == null || !ttsReady) return;
        tts.stop(); // đảm bảo không chồng âm
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
