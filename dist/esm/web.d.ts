import { WebPlugin } from '@capacitor/core';
import type { KeyPressEvent, PluginListenerHandle, TvKeyCapturePlugin } from './definitions';
export declare class TvKeyCaptureWeb extends WebPlugin implements TvKeyCapturePlugin {
    addListener(_eventName: 'keyPress', _listenerFunc: (event: KeyPressEvent) => void): Promise<PluginListenerHandle>;
    removeAllListeners(): Promise<void>;
    enable(): Promise<void>;
    disable(): Promise<void>;
}
