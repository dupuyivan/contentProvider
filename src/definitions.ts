export interface ContentProviderHandlerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  test( options: { name: string }): Promise<{ result: any }>;
  getValues(): Promise<{ result: any[] }>;
  insertValue( options: { name: string }): Promise<{ result:any }>;
  deleteValue( options: { id: string }): Promise<{ result:any }>;
  updateValue( options: { id: string, name: string }): Promise<{ result:any }>;
  addListener( evtName: string, callback: (a:void)=> any ): void;
}
