export interface TvKeyCapturePlugin {
  /**
   * Add a listener for key press events.
   * Only captures BACK and LEFT ARROW keys when the app is in foreground.
   *
   * @returns A promise that resolves with a PluginListenerHandle
   */
  addListener(
    eventName: 'keyPress',
    listenerFunc: (event: KeyPressEvent) => void
  ): Promise<PluginListenerHandle>;

  /**
   * Remove all listeners for key press events.
   */
  removeAllListeners(): Promise<void>;
}

export interface KeyPressEvent {
  /**
   * The key code as defined in Android KeyEvent
   */
  keyCode: number;

  /**
   * The key name as a constant string for easy comparison
   * Possible values: 'BACK' or 'LEFT_ARROW'
   */
  keyName: 'BACK' | 'LEFT_ARROW';

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
export const KeyCodes = {
  BACK: 4, // KeyEvent.KEYCODE_BACK
  LEFT_ARROW: 21, // KeyEvent.KEYCODE_DPAD_LEFT
} as const;

/**
 * Key name constants for easy comparison in application code
 */
export const KeyNames = {
  BACK: 'BACK' as const,
  LEFT_ARROW: 'LEFT_ARROW' as const,
} as const;
