import { WebPlugin } from '@capacitor/core';
import { SquarePaymentPlugin } from './definitions';

export class SquarePaymentWeb extends WebPlugin implements SquarePaymentPlugin {
  constructor() {
    super({
      name: 'SquarePayment',
      platforms: ['web'],
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

const SquarePayment = new SquarePaymentWeb();

export { SquarePayment };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(SquarePayment);
