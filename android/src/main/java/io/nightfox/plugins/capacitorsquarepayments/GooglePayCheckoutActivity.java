package io.nightfox.plugins.capacitorsquarepayments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import sqip.Call;
import sqip.GooglePay;
import sqip.GooglePayNonceResult;

public class GooglePayCheckoutActivity extends AppCompatActivity {

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 12234;
    @Nullable
    private Call<GooglePayNonceResult> requestNonceCall;

    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions =
            new Wallet.WalletOptions.Builder().setEnvironment(
                WalletConstants.ENVIRONMENT_PRODUCTION
            ).build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        paymentsClient = createPaymentsClient(GooglePayCheckoutActivity.this);
         String merchantId = getIntent().getStringExtra("googleMerchantId");

        // Immediately start google pay entry activity

        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(
                GooglePay.createPaymentDataRequest(
                    merchantId,
                    TransactionInfo.newBuilder().setTotalPriceStatus(
                            WalletConstants.TOTAL_PRICE_STATUS_NOT_CURRENTLY_KNOWN
                    ).setCurrencyCode("USD").build()
                )
            ),
            this,
            LOAD_PAYMENT_DATA_REQUEST_CODE
        );

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentData paymentData = PaymentData.getFromIntent(data);
                if (paymentData != null && paymentData.getPaymentMethodToken() != null) {
                    String googlePayToken = paymentData.getPaymentMethodToken().getToken();

                    //Request a nonce, save Card on File, and take a payment
                    requestNonceCall = GooglePay.requestGooglePayNonce(googlePayToken);
                    requestNonceCall.enqueue(result -> {
                        if (result.isSuccess()) {
                            data.putExtra("nonce", result.getSuccessValue().getNonce());
                            setResult(RESULT_OK, data);
                        } else if (result.isError()) {
                            data.putExtra("error", result.getErrorValue().getMessage());
                        }
                        super.onActivityResult(requestCode, resultCode, data);
                        finish();
                    });
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
                finish();
            }
        }
    }


    public void showError(String message) {
        showOkDialog(R.string.unsuccessful_order, message);
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
