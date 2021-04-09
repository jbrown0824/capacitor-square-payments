package io.nightfox.plugins.capacitorsquarepayments;

import android.content.res.Resources;

import java.io.IOException;

import sqip.Call;
import sqip.CardDetails;
import sqip.CardEntryActivityCommand;
import sqip.CardNonceBackgroundHandler;

public class CardEntryBackgroundHandler implements CardNonceBackgroundHandler {

  @Override
  public CardEntryActivityCommand handleEnteredCardInBackground(CardDetails cardDetails) {
    return new CardEntryActivityCommand.Finish();

//    try {
      // TODO Call your backend service
//      MyBackendServiceResponse response = // myBackendService(cardDetails.getNonce());...

//      if (response.isSuccessful()) {
//        return new CardEntryActivityCommand.Finish();
//      } else {
//        return new CardEntryActivityCommand.ShowError(response.errorMessage)
//      }
//    } catch(IOException exception) {
//      return new CardEntryActivityCommand.ShowError(
//              resources.getString(R.string.network_failure));
//    }
  }

//  private final ChargeCall.Factory chargeCallFactory;
//  private final Resources resources;
//
//  public CardEntryBackgroundHandler(ChargeCall.Factory chargeCallFactory,
//      Resources resources) {
//    this.chargeCallFactory = chargeCallFactory;
//    this.resources = resources;
//  }
//
//  @Override
//  public CardEntryActivityCommand handleEnteredCardInBackground(CardDetails cardDetails) {
//    if (!ConfigHelper.serverHostSet()) {
//      ConfigHelper.printCurlCommand(cardDetails.getNonce());
//      return new CardEntryActivityCommand.Finish();
//    }
//
//    Call<ChargeResult> chargeCall = chargeCallFactory.create(cardDetails.getNonce());
//    ChargeResult chargeResult = chargeCall.execute();
//
//    if (chargeResult.success) {
//      return new CardEntryActivityCommand.Finish();
//    } else if (chargeResult.networkError) {
//      return new CardEntryActivityCommand.ShowError(resources.getString(R.string.network_failure));
//    } else {
//      return new CardEntryActivityCommand.ShowError(chargeResult.errorMessage);
//    }
//  }
}
