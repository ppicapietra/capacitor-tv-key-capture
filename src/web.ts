import { WebPlugin } from '@capacitor/core';

import type { KeyPressEvent, PluginListenerHandle, TvKeyCapturePlugin } from './definitions';

export class TvKeyCaptureWeb extends WebPlugin implements TvKeyCapturePlugin {
  async addListener(
    _eventName: 'keyPress',
    _listenerFunc: (event: KeyPressEvent) => void
  ): Promise<PluginListenerHandle> {
    console.warn('TvKeyCapture: addListener() no está disponible en web. Solo funciona en Android TV.');
    throw this.unimplemented('addListener() no está implementado en web. Usa Android TV.');
  }

  async removeAllListeners(): Promise<void> {
    console.warn('TvKeyCapture: removeAllListeners() no está disponible en web.');
    throw this.unimplemented('removeAllListeners() no está implementado en web.');
  }

  async enable(): Promise<void> {
    console.warn('TvKeyCapture: enable() no está disponible en web.');
    throw this.unimplemented('enable() no está implementado en web.');
  }

  async disable(): Promise<void> {
    console.warn('TvKeyCapture: disable() no está disponible en web.');
    throw this.unimplemented('disable() no está implementado en web.');
  }
}
