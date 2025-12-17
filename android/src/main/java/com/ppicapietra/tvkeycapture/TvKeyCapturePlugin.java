package com.ppicapietra.tvkeycapture;

import android.view.KeyEvent;
import android.app.Activity;
import android.view.View;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TvKeyCapture")
public class TvKeyCapturePlugin extends Plugin {

    // Key codes we're interested in capturing
    private static final int KEYCODE_BACK = KeyEvent.KEYCODE_BACK;
    private static final int KEYCODE_DPAD_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;

    private View.OnKeyListener keyListener;

    @Override
    public void load() {
        // Create key listener that will be attached to the root view
        keyListener = (v, keyCode, event) -> {
            // Only handle key down events
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                return handleKeyEvent(keyCode);
            }
            return false;
        };

        // Set up key event handler for the activity
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                attachKeyListener(activity);
            });
        }
    }

    /**
     * Attach key listener to the root view of the activity
     */
    private void attachKeyListener(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        if (rootView != null) {
            rootView.setFocusableInTouchMode(true);
            rootView.setFocusable(true);
            rootView.requestFocus();
            rootView.setOnKeyListener(keyListener);
        }
    }

    /**
     * Handle key events and notify listeners if it's a key we're interested in
     */
    private boolean handleKeyEvent(int keyCode) {
        // Only process BACK and LEFT ARROW keys
        if (keyCode != KEYCODE_BACK && keyCode != KEYCODE_DPAD_LEFT) {
            return false;
        }

        // Only process when app is in foreground
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        // Determine key name
        String keyName;
        if (keyCode == KEYCODE_BACK) {
            keyName = "BACK";
        } else {
            keyName = "LEFT_ARROW";
        }

        // Create event object
        JSObject eventData = new JSObject();
        eventData.put("keyCode", keyCode);
        eventData.put("keyName", keyName);
        eventData.put("timestamp", System.currentTimeMillis());

        // Notify all active listeners using Capacitor's built-in listener system
        notifyListeners("keyPress", eventData);

        // Return false to allow normal key handling to continue
        // Return true if you want to consume the event and prevent default behavior
        return false;
    }

    @Override
    protected void handleOnResume() {
        // Re-attach listener when activity resumes (app comes to foreground)
        Activity activity = getActivity();
        if (activity != null && keyListener != null) {
            activity.runOnUiThread(() -> {
                attachKeyListener(activity);
            });
        }
    }
}
