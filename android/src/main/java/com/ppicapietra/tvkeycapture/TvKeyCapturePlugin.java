package com.ppicapietra.tvkeycapture;

import android.util.Log;
import android.view.KeyEvent;
import android.app.Activity;
import android.view.View;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.Bridge;
import com.getcapacitor.PluginHandle;
import com.getcapacitor.PluginCall;

@CapacitorPlugin(name = "TvKeyCapture")
public class TvKeyCapturePlugin extends Plugin {

    private static final String TAG = "TvKeyCapturePlugin";

    // Key codes we're interested in capturing
    private static final int KEYCODE_BACK = KeyEvent.KEYCODE_BACK;
    private static final int KEYCODE_DPAD_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
    private static final int KEYCODE_DPAD_DOWN = KeyEvent.KEYCODE_DPAD_DOWN;
    private static final int KEYCODE_DPAD_UP = KeyEvent.KEYCODE_DPAD_UP;
    private static final int KEYCODE_DPAD_CENTER = KeyEvent.KEYCODE_DPAD_CENTER; // OK button

    private View.OnKeyListener keyListener;
    private boolean isListenerAttached = false;
    private boolean isEnabled = false;

    @Override
    public void load() {
        Log.d(TAG, "Plugin load() called");
        // Create key listener that will be attached to multiple views
        keyListener = (v, keyCode, event) -> {
            // Log ALL key events for debugging
            String keyName = KeyEvent.keyCodeToString(keyCode);
            Log.d(TAG, String.format("Key event detected on view - keyCode: %d (%s), action: %s, enabled: %s", 
                keyCode, keyName, 
                event.getAction() == KeyEvent.ACTION_DOWN ? "DOWN" : "UP",
                isEnabled));
            
            // Only handle if enabled and key down events
            if (isEnabled && event.getAction() == KeyEvent.ACTION_DOWN) {
                return handleKeyEvent(keyCode);
            }
            return false;
        };

        Log.d(TAG, "Listener created but not attached yet (will be attached when session is active)");
    }

    /**
     * Attach key listener to multiple views for better coverage
     */
    private void attachKeyListener(Activity activity) {
        if (isListenerAttached) {
            Log.d(TAG, "Listener already attached, skipping");
            return;
        }
        
        try {
            // Try attaching to decor view
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                Log.d(TAG, "Attaching key listener to decor view");
                decorView.setFocusableInTouchMode(true);
                decorView.setFocusable(true);
                decorView.requestFocus();
                decorView.setOnKeyListener(keyListener);
            }
            
            // Also try root view
            View rootView = decorView != null ? decorView.getRootView() : null;
            if (rootView != null && rootView != decorView) {
                Log.d(TAG, "Attaching key listener to root view");
                rootView.setFocusableInTouchMode(true);
                rootView.setFocusable(true);
                rootView.setOnKeyListener(keyListener);
            }
            
            // Try attaching to the content view (WebView container)
            View contentView = activity.findViewById(android.R.id.content);
            if (contentView != null) {
                Log.d(TAG, "Attaching key listener to content view");
                contentView.setFocusableInTouchMode(true);
                contentView.setFocusable(true);
                contentView.setOnKeyListener(keyListener);
            }
            
            // Also try to get the WebView from the bridge
            Bridge bridge = getBridge();
            if (bridge != null) {
                View webView = bridge.getWebView();
                if (webView != null) {
                    Log.d(TAG, "Attaching key listener to WebView");
                    webView.setFocusableInTouchMode(true);
                    webView.setFocusable(true);
                    webView.setOnKeyListener(keyListener);
                }
            }
            
            isListenerAttached = true;
            Log.d(TAG, "Key listener attached successfully to multiple views");
        } catch (Exception e) {
            Log.e(TAG, "Error attaching key listener: " + e.getMessage(), e);
        }
    }

    /**
     * Detach key listener from views
     */
    private void detachKeyListener(Activity activity) {
        if (!isListenerAttached) {
            Log.d(TAG, "Listener not attached, skipping detach");
            return;
        }
        
        try {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                decorView.setOnKeyListener(null);
            }
            
            View rootView = decorView != null ? decorView.getRootView() : null;
            if (rootView != null && rootView != decorView) {
                rootView.setOnKeyListener(null);
            }
            
            View contentView = activity.findViewById(android.R.id.content);
            if (contentView != null) {
                contentView.setOnKeyListener(null);
            }
            
            Bridge bridge = getBridge();
            if (bridge != null) {
                View webView = bridge.getWebView();
                if (webView != null) {
                    webView.setOnKeyListener(null);
                }
            }
            
            isListenerAttached = false;
            Log.d(TAG, "Key listener detached successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error detaching key listener: " + e.getMessage(), e);
        }
    }

    /**
     * Handle key events and notify listeners if it's a key we're interested in
     */
    private boolean handleKeyEvent(int keyCode) {
        Log.d(TAG, String.format("handleKeyEvent called with keyCode: %d, isEnabled: %s", keyCode, isEnabled));
        
        if (!isEnabled) {
            Log.d(TAG, "Key capture is disabled, ignoring");
            return false;
        }
        
        // Only process BACK, LEFT ARROW, DOWN, UP, and OK (DPAD_CENTER) keys
        if (keyCode != KEYCODE_BACK && keyCode != KEYCODE_DPAD_LEFT 
            && keyCode != KEYCODE_DPAD_DOWN && keyCode != KEYCODE_DPAD_UP
            && keyCode != KEYCODE_DPAD_CENTER) {
            Log.d(TAG, String.format("Key %d is not BACK, LEFT_ARROW, ARROW_DOWN, ARROW_UP, or OK, ignoring", keyCode));
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
        } else if (keyCode == KEYCODE_DPAD_LEFT) {
            keyName = "LEFT_ARROW";
        } else if (keyCode == KEYCODE_DPAD_DOWN) {
            keyName = "ARROW_DOWN";
        } else if (keyCode == KEYCODE_DPAD_UP) {
            keyName = "ARROW_UP";
        } else if (keyCode == KEYCODE_DPAD_CENTER) {
            keyName = "OK";
        } else {
            keyName = "UNKNOWN";
        }

        Log.d(TAG, String.format("Processing %s key (keyCode: %d)", keyName, keyCode));

        // Create event object
        JSObject eventData = new JSObject();
        eventData.put("keyCode", keyCode);
        eventData.put("keyName", keyName);
        eventData.put("timestamp", System.currentTimeMillis());

        // Notify all active listeners using Capacitor's built-in listener system
        Log.d(TAG, String.format("Notifying listeners about %s key press", keyName));
        
        // Log event data safely
        try {
            Log.d(TAG, String.format("Event data: keyCode=%d, keyName=%s, timestamp=%d", 
                eventData.getInt("keyCode"), 
                eventData.getString("keyName"), 
                eventData.getLong("timestamp")));
        } catch (Exception e) {
            Log.w(TAG, "Error logging event data: " + e.getMessage());
        }
        
        // Check if there are any listeners registered
        Bridge bridge = getBridge();
        if (bridge != null) {
            PluginHandle pluginHandle = bridge.getPlugin("TvKeyCapture");
            if (pluginHandle != null) {
                Log.d(TAG, "Plugin handle found, checking listeners...");
                
                // Try to check if listeners are registered
                try {
                    // Use reflection to check listener count if possible
                    java.lang.reflect.Method getListenersMethod = pluginHandle.getClass().getMethod("getListeners", String.class);
                    if (getListenersMethod != null) {
                        Object listeners = getListenersMethod.invoke(pluginHandle, "keyPress");
                        Log.d(TAG, "Listeners for 'keyPress': " + (listeners != null ? listeners.toString() : "null"));
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Could not check listener count (this is normal): " + e.getMessage());
                }
                
                Log.d(TAG, "Calling notifyListeners() for event 'keyPress'...");
            } else {
                Log.w(TAG, "Plugin handle is null!");
            }
        } else {
            Log.w(TAG, "Bridge is null!");
        }
        
        try {
            Log.d(TAG, "Event data being sent: " + eventData.toString());
            Log.d(TAG, "About to call notifyListeners('keyPress', eventData)...");
            notifyListeners("keyPress", eventData);
            Log.d(TAG, "notifyListeners() called successfully, checking if listeners received the event...");
        } catch (Exception e) {
            Log.e(TAG, "❌ Error calling notifyListeners(): " + e.getMessage(), e);
            e.printStackTrace();
        }

        // For BACK key, we need to consume the event to prevent Android's default behavior
        // (closing the activity) from executing before JavaScript receives the event.
        // For other keys, we also consume them to ensure JavaScript receives them reliably.
        if (keyCode == KEYCODE_BACK) {
            Log.d(TAG, "Consuming BACK key event to prevent default behavior");
            return true; // Consume the event to prevent Activity from closing
        }
        
        // For navigation keys (LEFT_ARROW, ARROW_DOWN, ARROW_UP), consume them when modal is open
        // to ensure JavaScript receives them. When modal is closed, LEFT_ARROW should be consumed
        // to open the modal, but navigation keys can be allowed to propagate.
        // For now, consume all to ensure reliable delivery to JavaScript
        Log.d(TAG, String.format("Consuming %s key event to ensure JavaScript receives it", keyName));
        return true;
    }

    /**
     * Public method to handle key events from Activity (for MainActivity integration)
     * This can be called from MainActivity.dispatchKeyEvent() or onKeyDown()
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isEnabled || !isListenerAttached) {
            return false; // Let the event propagate
        }
        
        Log.d(TAG, String.format("onKeyDown called from Activity - keyCode: %d", keyCode));
        return handleKeyEvent(keyCode);
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
        Log.d(TAG, "========== enable() called from JS ==========");
        Log.d(TAG, "isEnabled before: " + isEnabled);
        Log.d(TAG, "isListenerAttached before: " + isListenerAttached);
        
        // Check if listeners are registered
        Bridge bridge = getBridge();
        if (bridge != null) {
            PluginHandle pluginHandle = bridge.getPlugin("TvKeyCapture");
            if (pluginHandle != null) {
                Log.d(TAG, "Plugin handle found for TvKeyCapture");
            } else {
                Log.w(TAG, "WARNING: Plugin handle is null - listeners may not be registered yet!");
            }
        }
        
        isEnabled = true;
        Activity activity = getActivity();
        if (activity != null && keyListener != null) {
            activity.runOnUiThread(() -> {
                attachKeyListener(activity);
                Log.d(TAG, "Key capture enabled and listener attached");
                Log.d(TAG, "isEnabled after: " + isEnabled);
                Log.d(TAG, "isListenerAttached after: " + isListenerAttached);
            });
            call.resolve();
            Log.d(TAG, "enable() resolved successfully");
        } else {
            Log.e(TAG, "Cannot enable: activity=" + (activity != null) + ", keyListener=" + (keyListener != null));
            call.reject("Activity or keyListener is null");
        }
        Log.d(TAG, "========== enable() finished ==========");
    }

    /**
     * Disable key capture (called from JS when session ends)
     */
    @com.getcapacitor.PluginMethod
    public void disable(com.getcapacitor.PluginCall call) {
        Log.d(TAG, "disable() called from JS");
        isEnabled = false;
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                detachKeyListener(activity);
                Log.d(TAG, "Key capture disabled and listener detached");
            });
            call.resolve();
        } else {
            call.reject("Activity is null");
        }
    }
}
