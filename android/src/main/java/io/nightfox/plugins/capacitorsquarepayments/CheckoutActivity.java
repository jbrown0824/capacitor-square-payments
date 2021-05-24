package io.nightfox.plugins.capacitorsquarepayments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import sqip.Card;
import sqip.CardDetails;
import sqip.CardEntry;
import sqip.GooglePay;
import sqip.InAppPaymentsSdk;

import static sqip.CardEntry.DEFAULT_CARD_ENTRY_REQUEST_CODE;

public class CheckoutActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Immediately start card entry activity
    CardEntry.startCardEntryActivity(CheckoutActivity.this, true,
            DEFAULT_CARD_ENTRY_REQUEST_CODE);

    super.onCreate(savedInstanceState);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    CardEntry.handleActivityResult(data, result -> {
      if (result.isSuccess()) {
        CardDetails cardResult = result.getSuccessValue();
        Card card = cardResult.getCard();
        String nonce = cardResult.getNonce();
        data.putExtra("nonce", nonce);
        setResult(RESULT_OK, data);
      } else if (result.isCanceled()) {
        setResult(RESULT_CANCELED, data);
        Log.d("canceled", "canceled");
      }
      super.onActivityResult(requestCode, resultCode, data);
      finish();
    });
  }

  public void showError(String message) {
    showOkDialog(R.string.unsuccessful_order, message);
  }

  public void showSuccessCharge() {
    showOkDialog(R.string.successful_order_title, getString(R.string.successful_order_message));
  }

  public void showServerHostNotSet() {
    showOkDialog(R.string.server_host_not_set_title, Html.fromHtml(getString(R.string.server_host_not_set_message)));
  }

  private void showMerchantIdNotSet() {
    showOkDialog(R.string.merchant_id_not_set_title, Html.fromHtml(getString(R.string.merchant_id_not_set_message)));
  }

  private void showOkDialog(int titleResId, CharSequence message) {
    new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
            .setTitle(titleResId)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show();
  }

  public void showNetworkErrorRetryPayment(Runnable retry) {
    new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
            .setTitle(R.string.network_failure_title)
            .setMessage(getString(R.string.network_failure))
            .setPositiveButton(R.string.retry, (dialog, which) -> retry.run())
            .show();
  }
}
