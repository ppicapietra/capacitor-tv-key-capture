import { registerPlugin } from '@capacitor/core';
const TvKeyCapture = registerPlugin('TvKeyCapture', {
    web: () => import('./web').then((m) => new m.TvKeyCaptureWeb()),
});
export * from './definitions';
export { TvKeyCapture };
//# sourceMappingURL=index.js.map