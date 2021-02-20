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

            let view = ExampleViewController(call: call, amount: _amount, appleMerchantId: self.APPLE_MERCHANT_ID, appleCountryCode: self.APPLE_COUNTRY_CODE, appleCurrencyCode: self.APPLE_CURRENCY_CODE)
            self.bridge.viewController.present(view, animated: true, completion: nil)


//            let evController = ExampleViewController(call: call, amount: _amount)
////            self.bridge.viewController.present(view, animated: true, completion: nil)
//            let orderViewController = OrderViewController(amount: _amount)
//            orderViewController.delegate = evController;
//            let nc = OrderNavigationController(rootViewController: orderViewController)
//            nc.modalPresentationStyle = .custom
//            nc.transitioningDelegate = evController
//            self.bridge.viewController.present(nc, animated: true, completion: nil)

            // TODO - This is how you hide once the payment nonce has been received
//            self.bridge.viewController.dismiss(animated: true)
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
