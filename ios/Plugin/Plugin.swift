import Foundation
import Capacitor
import SquareInAppPaymentsSDK

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SquarePayment)
public class SquarePayment: CAPPlugin {

    @objc func requestNonce(_ call: CAPPluginCall) {
        guard let _amount = call.options["amount"] as? Int else {
			return call.reject("Must provide amount")
		}

        DispatchQueue.main.async {
        	print("opening payment")
        	print(_amount)
            let view = ExampleViewController(call: call, amount: _amount)
            self.bridge.viewController.present(view, animated: true, completion: nil)
            // TODO - This is how you hide once the payment nonce has been received
//            self.bridge.viewController.dismiss(animated: true)
        }
    }

    @objc func initApp(_ call: CAPPluginCall) {
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
