export interface TvKeyCapturePlugin {
  /**
   * Add a listener for key press events.
   * Captures BACK, LEFT ARROW, ARROW_DOWN, and ARROW_UP keys when the app is in foreground.
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
   * Possible values: 'BACK', 'LEFT_ARROW', 'ARROW_DOWN', or 'ARROW_UP'
   */
  keyName: 'BACK' | 'LEFT_ARROW' | 'ARROW_DOWN' | 'ARROW_UP';

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
  ARROW_DOWN: 20, // KeyEvent.KEYCODE_DPAD_DOWN
  ARROW_UP: 19, // KeyEvent.KEYCODE_DPAD_UP
} as const;

/**
 * Key name constants for easy comparison in application code
 */
export const KeyNames = {
  BACK: 'BACK' as const,
  LEFT_ARROW: 'LEFT_ARROW' as const,
  ARROW_DOWN: 'ARROW_DOWN' as const,
  ARROW_UP: 'ARROW_UP' as const,
} as const;
