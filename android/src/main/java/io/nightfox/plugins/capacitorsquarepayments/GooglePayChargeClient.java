package io.nightfox.plugins.capacitorsquarepayments;

import androidx.annotation.Nullable;
import sqip.Call;
import sqip.GooglePay;
import sqip.GooglePayNonceResult;

public class GooglePayChargeClient {
  private final ChargeCall.Factory chargeCallFactory;

  @Nullable private io.nightfox.plugins.capacitorsquarepayments.GooglePayCheckoutActivity activity;
  @Nullable private Call<GooglePayNonceResult> requestNonceCall;
  @Nullable private Call<io.nightfox.plugins.capacitorsquarepayments.ChargeResult> chargeCall;

  GooglePayChargeClient(ChargeCall.Factory chargeCallFactory) {
    this.chargeCallFactory = chargeCallFactory;
  }

  public void createNonce(String googlePayToken) {
    if (nonceRequestInFlight() || chargeRequestInFlight()) {
      return;
    }
    requestNonceCall = GooglePay.requestGooglePayNonce(googlePayToken);
    requestNonceCall.enqueue(result -> onNonceRequestResult(googlePayToken, result));
  }

  private void onNonceRequestResult(String googlePayToken, GooglePayNonceResult result) {
    if (!nonceRequestInFlight()) {
      return;
    }
    requestNonceCall = null;
    if (activity == null) {
      return;
    }
    if (result.isSuccess()) {
      String nonce = result.getSuccessValue().getNonce();
//      chargeNonce(nonce);
    } else if (result.isError()) {
      GooglePayNonceResult.Error error = result.getErrorValue();
      switch (error.getCode()) {
        case NO_NETWORK:
          activity.showNetworkErrorRetryPayment(() -> createNonce(googlePayToken));
          break;
        case UNSUPPORTED_SDK_VERSION:
        case USAGE_ERROR:
          activity.showError(error.getMessage());
      }
    }
  }

  public void cancel() {
    if (nonceRequestInFlight()) {
      requestNonceCall.cancel();
      requestNonceCall = null;
    }
    if (chargeRequestInFlight()) {
      chargeCall.cancel();
      chargeCall = null;
    }
  }

  public void onActivityCreated(io.nightfox.plugins.capacitorsquarepayments.GooglePayCheckoutActivity activity) {
    this.activity = activity;
  }

  public void onActivityDestroyed() {
    activity = null;
  }

  private boolean chargeRequestInFlight() {
    return chargeCall != null;
  }

  private boolean nonceRequestInFlight() {
    return requestNonceCall != null;
  }

}
