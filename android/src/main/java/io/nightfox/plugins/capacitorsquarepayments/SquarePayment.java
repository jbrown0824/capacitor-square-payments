package io.nightfox.plugins.capacitorsquarepayments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import sqip.CardEntry;
import sqip.InAppPaymentsSdk;

@NativePlugin(
        requestCodes={SquarePayment.REQUEST_PAYMENT} // register request code(s) for intent results
)
public class SquarePayment extends Plugin {

    protected static final int REQUEST_PAYMENT = 12345; //

    public String GOOGLE_PAY_MERCHANT_ID = "REPLACE_ME";


    public void load() {
        // Called when the plugin is first constructed in the bridge
        CardEntryBackgroundHandler cardHandler = new CardEntryBackgroundHandler();
        CardEntry.setCardNonceBackgroundHandler(cardHandler);
    }

    @PluginMethod
    public void createAlarm(PluginCall call) {
        saveCall(call);

        Intent intent = new Intent(getActivity(), CheckoutActivity.class);

        startActivityForResult(call, intent, SquarePayment.REQUEST_PAYMENT);
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        // Get the previously saved call
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        if (requestCode == REQUEST_PAYMENT) {
            // Do something with the data
            savedCall.reject("Payment Completed");
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

//        InAppPaymentsSdk.setSquareApplicationId(call.getString("applicationId"));

        this.GOOGLE_PAY_MERCHANT_ID = call.getString("googlePayMerchantId");
    }

    @PluginMethod
    public void requestNonce(PluginCall call) {

    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }
}
