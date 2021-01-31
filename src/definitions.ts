declare module '@capacitor/core' {
  interface PluginRegistry {
    SquarePayment: SquarePaymentPlugin;
  }
}

export interface SquarePaymentPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
