export interface TvKeyCapturePlugin {
    /**
     * Add a listener for key press events.
     * Captures BACK, LEFT ARROW, ARROW_DOWN, and ARROW_UP keys when the app is in foreground.
     *
     * @returns A promise that resolves with a PluginListenerHandle
     */
    addListener(eventName: 'keyPress', listenerFunc: (event: KeyPressEvent) => void): Promise<PluginListenerHandle>;
    /**
     * Remove all listeners for key press events.
     */
    removeAllListeners(): Promise<void>;
    /**
     * Enable key capture (call this when session is active)
     */
    enable(): Promise<void>;
    /**
     * Disable key capture (call this when session ends)
     */
    disable(): Promise<void>;
}
export interface KeyPressEvent {
    /**
     * The key code as defined in Android KeyEvent
     */
    keyCode: number;
    /**
     * The key name as a constant string for easy comparison
     * Possible values: 'BACK', 'LEFT_ARROW', 'ARROW_DOWN', 'ARROW_UP', or 'OK'
     */
    keyName: 'BACK' | 'LEFT_ARROW' | 'ARROW_DOWN' | 'ARROW_UP' | 'OK';
    /**
     * Timestamp when the key was pressed (milliseconds since epoch)
     */
    timestamp: number;
}
export interface PluginListenerHandle {
    remove(): Promise<void>;
}
/**
 * Key code constants for easy comparison in application code
 */
export declare const KeyCodes: {
    readonly BACK: 4;
    readonly LEFT_ARROW: 21;
    readonly ARROW_DOWN: 20;
    readonly ARROW_UP: 19;
    readonly OK: 23;
};
/**
 * Key name constants for easy comparison in application code
 */
export declare const KeyNames: {
    readonly BACK: "BACK";
    readonly LEFT_ARROW: "LEFT_ARROW";
    readonly ARROW_DOWN: "ARROW_DOWN";
    readonly ARROW_UP: "ARROW_UP";
    readonly OK: "OK";
};
