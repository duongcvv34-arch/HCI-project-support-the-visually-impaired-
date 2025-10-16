package com.example.newvoice;

import android.os.Handler;
import android.view.View;

public abstract class DoubleTapListener implements View.OnClickListener {

    private static final long DOUBLE_TAP_TIMEOUT = 400; // Thời gian tối đa giữa 2 lần bấm (ms)
    private boolean waitingForSecondTap = false;
    private final Handler handler = new Handler();

    @Override
    public void onClick(View v) {
        if (waitingForSecondTap) {
            waitingForSecondTap = false;
            handler.removeCallbacksAndMessages(null);
            onDoubleTap(v);
        } else {
            waitingForSecondTap = true;
            handler.postDelayed(() -> waitingForSecondTap = false, DOUBLE_TAP_TIMEOUT);
            onSingleTap(v);
        }
    }

    public abstract void onSingleTap(View v);
    public abstract void onDoubleTap(View v);
}
