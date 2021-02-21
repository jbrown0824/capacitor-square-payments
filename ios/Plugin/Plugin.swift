import Foundation
import Capacitor
import SquareInAppPaymentsSDK

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SquarePayment)
public class SquarePayment: CAPPlugin {

    var APPLE_MERCHANT_ID: String? = nil
    var APPLE_COUNTRY_CODE: String = "US"
    var APPLE_CURRENCY_CODE: String = "USD"

    @objc func requestNonce(_ call: CAPPluginCall) {
        guard let _amount = call.options["amount"] as? Int else {
            return call.reject("Must provide amount")
        }

        DispatchQueue.main.async {
            print("opening payment")
            print(_amount)

            let view = ExampleViewController(completed: { nonce in
                self.bridge.viewController.dismiss(animated: true)
                call.resolve([
                    "nonce": nonce
                ])
            }, amount: _amount, appleMerchantId: self.APPLE_MERCHANT_ID, appleCountryCode: self.APPLE_COUNTRY_CODE, appleCurrencyCode: self.APPLE_CURRENCY_CODE)
            self.bridge.viewController.present(view, animated: true, completion: nil)

        }
    }

    @objc func initApp(_ call: CAPPluginCall) {
        if let _appleMerchantId = call.getString("appleMerchantId") {
            self.APPLE_MERCHANT_ID = _appleMerchantId;
        }

        if let _appleCountryCode = call.getString("appleCountryCode") {
            self.APPLE_COUNTRY_CODE = _appleCountryCode;
        }

        if let _appleCurrencyCode = call.getString("appleCurrencyCode") {
            self.APPLE_CURRENCY_CODE = _appleCurrencyCode;
        }

        if let applicationId = call.getString("applicationId") {
            SQIPInAppPaymentsSDK.squareApplicationID = applicationId
            call.resolve([
                "message": "set applicationId"
            ]);
        } else {
            call.reject("applicationId null")
        }
    }
}
