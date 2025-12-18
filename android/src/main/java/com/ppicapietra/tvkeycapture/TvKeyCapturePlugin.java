package com.ppicapietra.tvkeycapture;

import android.util.Log;
import android.view.KeyEvent;
import android.app.Activity;
import android.view.View;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TvKeyCapture")
public class TvKeyCapturePlugin extends Plugin {

    private static final String TAG = "TvKeyCapturePlugin";

    // Key codes we're interested in capturing
    private static final int KEYCODE_BACK = KeyEvent.KEYCODE_BACK;
    private static final int KEYCODE_DPAD_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;

    private View.OnKeyListener keyListener;
    private boolean isListenerAttached = false;

    @Override
    public void load() {
        Log.d(TAG, "Plugin load() called");
        // Create key listener that will be attached to the root view
        keyListener = (v, keyCode, event) -> {
            // Log ALL key events for debugging
            String keyName = KeyEvent.keyCodeToString(keyCode);
            Log.d(TAG, String.format("Key event detected - keyCode: %d (%s), action: %s", 
                keyCode, keyName, 
                event.getAction() == KeyEvent.ACTION_DOWN ? "DOWN" : "UP"));
            
            // Only handle key down events
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                return handleKeyEvent(keyCode);
            }
            return false;
        };

        // Don't attach listener on load - wait for explicit activation from JS
        Log.d(TAG, "Listener created but not attached yet (will be attached when session is active)");
    }

    /**
     * Attach key listener to the root view of the activity
     */
    private void attachKeyListener(Activity activity) {
        if (isListenerAttached) {
            Log.d(TAG, "Listener already attached, skipping");
            return;
        }
        
        View rootView = activity.getWindow().getDecorView().getRootView();
        if (rootView != null) {
            Log.d(TAG, "Attaching key listener to root view");
            rootView.setFocusableInTouchMode(true);
            rootView.setFocusable(true);
            rootView.requestFocus();
            rootView.setOnKeyListener(keyListener);
            isListenerAttached = true;
            Log.d(TAG, "Key listener attached successfully");
        } else {
            Log.e(TAG, "Root view is null, cannot attach listener");
        }
    }

    /**
     * Detach key listener from the root view
     */
    private void detachKeyListener(Activity activity) {
        if (!isListenerAttached) {
            Log.d(TAG, "Listener not attached, skipping detach");
            return;
        }
        
        View rootView = activity.getWindow().getDecorView().getRootView();
        if (rootView != null) {
            Log.d(TAG, "Detaching key listener from root view");
            rootView.setOnKeyListener(null);
            isListenerAttached = false;
            Log.d(TAG, "Key listener detached successfully");
        }
    }

    /**
     * Handle key events and notify listeners if it's a key we're interested in
     */
    private boolean handleKeyEvent(int keyCode) {
        Log.d(TAG, String.format("handleKeyEvent called with keyCode: %d", keyCode));
        
        // Only process BACK and LEFT ARROW keys
        if (keyCode != KEYCODE_BACK && keyCode != KEYCODE_DPAD_LEFT) {
            Log.d(TAG, String.format("Key %d is not BACK or LEFT_ARROW, ignoring", keyCode));
            return false;
        }

        // Only process when app is in foreground
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            Log.w(TAG, "Activity is null or finishing, ignoring key event");
            return false;
        }

        // Determine key name
        String keyName;
        if (keyCode == KEYCODE_BACK) {
            keyName = "BACK";
        } else {
            keyName = "LEFT_ARROW";
        }

        Log.d(TAG, String.format("Processing %s key (keyCode: %d)", keyName, keyCode));

        // Create event object
        JSObject eventData = new JSObject();
        eventData.put("keyCode", keyCode);
        eventData.put("keyName", keyName);
        eventData.put("timestamp", System.currentTimeMillis());

        // Notify all active listeners using Capacitor's built-in listener system
        Log.d(TAG, String.format("Notifying listeners about %s key press", keyName));
        notifyListeners("keyPress", eventData);

        // Return false to allow normal key handling to continue
        // Return true if you want to consume the event and prevent default behavior
        return false;
    }

    @Override
    protected void handleOnResume() {
        Log.d(TAG, "handleOnResume() called");
        // Note: We don't auto-attach here anymore
        // The JS side will call enable() when session is active
    }

    /**
     * Enable key capture (called from JS when session is active)
     */
    @com.getcapacitor.PluginMethod
    public void enable(com.getcapacitor.PluginCall call) {
        Log.d(TAG, "enable() called from JS");
        Activity activity = getActivity();
        if (activity != null && keyListener != null) {
            activity.runOnUiThread(() -> {
                attachKeyListener(activity);
            });
            call.resolve();
        } else {
            Log.e(TAG, "Cannot enable: activity or keyListener is null");
            call.reject("Activity or keyListener is null");
        }
    }

    /**
     * Disable key capture (called from JS when session ends)
     */
    @com.getcapacitor.PluginMethod
    public void disable(com.getcapacitor.PluginCall call) {
        Log.d(TAG, "disable() called from JS");
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                detachKeyListener(activity);
            });
            call.resolve();
        } else {
            call.reject("Activity is null");
        }
    }
}
