package com.example.timeclock.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.timeclock.views.LoginActivity;

public class IdleEventHandler {
    private Handler handler;
    private Runnable runnable;
    private Activity activity;
    private final int timeoutInMS = 45000; //45 second inactivity timer

    public IdleEventHandler(Activity activity) {
        this.activity = activity;
    }

    public void onUserInteractionOccurred() {
        if (handler != null)
            resetHandler();
        else
            startHandler();
    }

    public void startHandler() {
        if (handler != null && runnable != null) {
            resetHandler();
        } else {
            handler = new Handler(Looper.getMainLooper());
            runnable = () -> {
                if (activity != null)
                    idleTimeoutExpired();
            };
            handler.postDelayed(runnable, timeoutInMS);
        }
    }

    private void resetHandler() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, timeoutInMS);
        }
    }

    public void stopHandler() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }

    private void idleTimeoutExpired() {
        stopHandler();
        endActivity();
    }

    private void endActivity() {
        if (activity != null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public void release() {
        handler = null;
        runnable = null;
        activity = null;
    }

}
