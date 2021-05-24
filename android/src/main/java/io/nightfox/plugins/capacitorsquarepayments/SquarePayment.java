package io.nightfox.plugins.capacitorsquarepayments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONObject;

import java.util.Optional;

import sqip.CardEntry;
import sqip.InAppPaymentsSdk;

import static android.app.Activity.RESULT_CANCELED;

@NativePlugin(
    requestCodes={SquarePayment.REQUEST_PAYMENT,SquarePayment.REQUEST_GOOGLE_PAYMENT} // register request code(s) for intent results
)
public class SquarePayment extends Plugin {

    protected static final int REQUEST_PAYMENT = 12345; //
    protected static final int REQUEST_GOOGLE_PAYMENT = 12346; //

    public String GOOGLE_PAY_MERCHANT_ID = "REPLACE_ME";

    public void load() {
        // Called when the plugin is first constructed in the bridge
        CardEntryBackgroundHandler cardHandler = new CardEntryBackgroundHandler();
        CardEntry.setCardNonceBackgroundHandler(cardHandler);
    }

    @PluginMethod
    public void requestManualEntryNonce(PluginCall call) {
        saveCall(call);

        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
        intent.putExtra("locationId", call.getString("locationId"));

        startActivityForResult(call, intent, SquarePayment.REQUEST_PAYMENT);
    }

    @PluginMethod
    public void requestGooglePayNonce(PluginCall call) {
        saveCall(call);

        final Optional<JSONObject> isReadyToPayJson = GooglePaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            call.reject("Unable to access Google Pay");
        }

        Intent intent = new Intent(getActivity(), GooglePayCheckoutActivity.class);
        intent.putExtra("googleMerchantId", GOOGLE_PAY_MERCHANT_ID);
        Log.d("GooglePay", "location id from call " + call.getString("locationId"));

        startActivityForResult(call, intent, SquarePayment.REQUEST_GOOGLE_PAYMENT);
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        // Get the previously saved call
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        if (requestCode == REQUEST_PAYMENT || requestCode == REQUEST_GOOGLE_PAYMENT) {
            // Do something with the data
            if (data != null && data.hasExtra("nonce")) {
                JSObject ret = new JSObject();
                ret.put("nonce", data.getStringExtra("nonce"));
                savedCall.resolve(ret);
            } else {
                if (resultCode == RESULT_CANCELED) {
                    savedCall.reject("User Cancelled");
                } else {
                    savedCall.reject("Cannot charge provided card");
                }
            }
        }
    }

    @PluginMethod
    public void initApp(PluginCall call) {
        if (!call.getData().has("applicationId")) {
            call.reject("applicationId null");
        }
        if (!call.getData().has("googlePayMerchantId")) {
            call.reject("googlePayMerchantId null");
        }

        // TODO - Check if this works, remove hard coded value in strings.xml
        InAppPaymentsSdk.setSquareApplicationId(call.getString("applicationId"));

        this.GOOGLE_PAY_MERCHANT_ID = call.getString("googlePayMerchantId");
    }

    @PluginMethod
    public void requestNonce(PluginCall call) {
        requestManualEntryNonce(call);
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }
}
