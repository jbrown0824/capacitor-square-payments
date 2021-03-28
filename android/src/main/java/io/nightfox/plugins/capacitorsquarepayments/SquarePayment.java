package io.nightfox.plugins.capacitorsquarepayments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import sqip.InAppPaymentsSdk;

@NativePlugin(
    permissions={
            Manifest.permission.SET_ALARM
    }
)
public class SquarePayment extends Plugin {

    public String GOOGLE_PAY_MERCHANT_ID = "REPLACE_ME";


    public void load() {
        // Called when the plugin is first constructed in the bridge
    }

    @PluginMethod
    public void createAlarm(PluginCall call) {
        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
        getActivity().startActivity(intent);
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
