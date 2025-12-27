import { WebPlugin } from '@capacitor/core';
export class TvKeyCaptureWeb extends WebPlugin {
    async addListener(_eventName, _listenerFunc) {
        console.warn('TvKeyCapture: addListener() no está disponible en web. Solo funciona en Android TV.');
        throw this.unimplemented('addListener() no está implementado en web. Usa Android TV.');
    }
    async removeAllListeners() {
        console.warn('TvKeyCapture: removeAllListeners() no está disponible en web.');
        throw this.unimplemented('removeAllListeners() no está implementado en web.');
    }
    async enable() {
        console.warn('TvKeyCapture: enable() no está disponible en web.');
        throw this.unimplemented('enable() no está implementado en web.');
    }
    async disable() {
        console.warn('TvKeyCapture: disable() no está disponible en web.');
        throw this.unimplemented('disable() no está implementado en web.');
    }
}
//# sourceMappingURL=web.js.map