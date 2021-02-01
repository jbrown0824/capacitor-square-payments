declare module '@capacitor/core' {
  interface PluginRegistry {
    SquarePayment: SquarePaymentPlugin;
  }
}

export interface SquarePaymentPlugin {
  initApp(options: { applicationId: string }): Promise<void>;
  requestNonce(_options: { amount: number }): Promise<{ nonce: string }>;
}
