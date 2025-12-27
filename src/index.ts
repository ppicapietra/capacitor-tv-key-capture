import { registerPlugin } from '@capacitor/core';

import type { TvKeyCapturePlugin } from './definitions';

const TvKeyCapture = registerPlugin<TvKeyCapturePlugin>('TvKeyCapture', {
  web: () => import('./web').then((m) => new m.TvKeyCaptureWeb()),
});

export * from './definitions';
export { TvKeyCapture };
