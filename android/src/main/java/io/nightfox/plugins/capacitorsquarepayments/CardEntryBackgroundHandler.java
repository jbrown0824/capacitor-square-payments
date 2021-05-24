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
  }
}
