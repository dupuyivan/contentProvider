import { WebPlugin } from '@capacitor/core';

import type { ContentProviderHandlerPlugin } from './definitions';

export class ContentProviderHandlerWeb extends WebPlugin implements ContentProviderHandlerPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
