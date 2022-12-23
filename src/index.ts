import { registerPlugin } from '@capacitor/core';

import type { ContentProviderHandlerPlugin } from './definitions';

const ContentProviderHandler = registerPlugin<ContentProviderHandlerPlugin>('ContentProviderHandler', {
  web: () => import('./web').then(m => new m.ContentProviderHandlerWeb()),
});

export * from './definitions';
export { ContentProviderHandler };
