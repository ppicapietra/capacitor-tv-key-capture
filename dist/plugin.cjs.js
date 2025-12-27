'use strict';

var core = require('@capacitor/core');

/**
 * Key code constants for easy comparison in application code
 */
const KeyCodes = {
    BACK: 4, // KeyEvent.KEYCODE_BACK
    LEFT_ARROW: 21, // KeyEvent.KEYCODE_DPAD_LEFT
    ARROW_DOWN: 20, // KeyEvent.KEYCODE_DPAD_DOWN
    ARROW_UP: 19, // KeyEvent.KEYCODE_DPAD_UP
    OK: 23, // KeyEvent.KEYCODE_DPAD_CENTER (OK button on remote)
};
/**
 * Key name constants for easy comparison in application code
 */
const KeyNames = {
    BACK: 'BACK',
    LEFT_ARROW: 'LEFT_ARROW',
    ARROW_DOWN: 'ARROW_DOWN',
    ARROW_UP: 'ARROW_UP',
    OK: 'OK',
};

const TvKeyCapture = core.registerPlugin('TvKeyCapture', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.TvKeyCaptureWeb()),
});

class TvKeyCaptureWeb extends core.WebPlugin {
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

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    TvKeyCaptureWeb: TvKeyCaptureWeb
});

exports.KeyCodes = KeyCodes;
exports.KeyNames = KeyNames;
exports.TvKeyCapture = TvKeyCapture;
//# sourceMappingURL=plugin.cjs.js.map
