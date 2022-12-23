import { WebPlugin } from '@capacitor/core';

import type { ContentProviderHandlerPlugin } from './definitions';

export class ContentProviderHandlerWeb extends WebPlugin implements ContentProviderHandlerPlugin {
  getValues(): Promise<{ result: any[]; }> {
    console.log('getValues');
    throw new Error('Method not implemented.');
  }
  insertValue(options: { name: string; }): Promise<{ result: any; }> {
    console.log('insertValue', options);
    throw new Error('Method not implemented.');
  }
  deleteValue(options: { id: string; }): Promise<{ result: any; }> {
    console.log('deleteValue', options);
    throw new Error('Method not implemented.');
  }
  updateValue(options: { id: string; name: string; }): Promise<{ result: any; }> {
    console.log('updateValue', options);
    throw new Error('Method not implemented.');
  }
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
  async test(options: { name: string; }): Promise<{ result: any; }> {
    console.log('test', options);
    return { result: {} }
  }
}
