export interface ContentProviderHandlerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
