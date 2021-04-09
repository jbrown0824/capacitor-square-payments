package io.nightfox.plugins.capacitorsquarepayments;

import android.app.Activity;
import android.app.Application;

import retrofit2.Retrofit;
import sqip.CardEntry;

public class CapacitorSquarePayment extends Application {

  public static GooglePayChargeClient createGooglePayChargeClient(Activity activity) {
    CapacitorSquarePayment application = (CapacitorSquarePayment) activity.getApplication();
    return new GooglePayChargeClient(application.chargeCallFactory);
  }

  private ChargeCall.Factory chargeCallFactory;

  @Override
  public void onCreate() {
    super.onCreate();

//    Retrofit retrofit = ConfigHelper.createRetrofitInstance();
//    chargeCallFactory = new ChargeCall.Factory(retrofit);

    // Original Version
//    CardEntryBackgroundHandler cardHandler =
//        new CardEntryBackgroundHandler(chargeCallFactory, getResources());
//    CardEntry.setCardNonceBackgroundHandler(cardHandler);

    // Readme Version
    CardEntryBackgroundHandler cardHandler = new CardEntryBackgroundHandler();
    CardEntry.setCardNonceBackgroundHandler(cardHandler);
  }
}
