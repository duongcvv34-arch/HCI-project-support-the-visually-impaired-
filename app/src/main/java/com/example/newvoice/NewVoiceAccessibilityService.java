package com.example.newvoice;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Handler;
import android.widget.Toast;

public class NewVoiceAccessibilityService extends AccessibilityService {

    private boolean waitingForSecondTap = false;
    private AccessibilityNodeInfo lastNode;
    private final Handler handler = new Handler();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Lắng nghe khi người dùng chạm vào một phần tử
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED ||
                event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {

            AccessibilityNodeInfo node = event.getSource();
            if (node == null) return;

            // Nếu đang chờ nhấn lần 2 → thực hiện hành động thật
            if (waitingForSecondTap && lastNode != null && node.equals(lastNode)) {
                waitingForSecondTap = false;
                handler.removeCallbacksAndMessages(null);

                // Gọi hành động click thật sự
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Toast.makeText(this, "Thực hiện hành động", Toast.LENGTH_SHORT).show();

            } else {
                // Lần nhấn đầu tiên: chỉ đọc tên phần tử
                waitingForSecondTap = true;
                lastNode = node;
                speak("Đã chọn " + node.getContentDescription());

                // Nếu sau 1 giây không nhấn lần 2 → reset
                handler.postDelayed(() -> {
                    waitingForSecondTap = false;
                    lastNode = null;
                }, 1000);
            }
        }
    }

    private void speak(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInterrupt() {
        // Khi bị dừng dịch vụ
    }
}
