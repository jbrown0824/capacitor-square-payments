import { WebPlugin } from '@capacitor/core';
import { SquarePaymentPlugin } from './definitions';

export class SquarePaymentWeb extends WebPlugin implements SquarePaymentPlugin {
  constructor() {
    super({
      name: 'SquarePayment',
      platforms: ['web'],
    });
  }

  async initApp(_options: { applicationId: string; }): Promise<void> {
    throw new Error("Method not implemented.");
    document.getElementById()
  }

  async requestNonce(_options: { amount: number }): Promise<{ nonce: string }> {
    throw new Error("Method not implemented");
  }
}

const SquarePayment = new SquarePaymentWeb();

export { SquarePayment };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(SquarePayment);
