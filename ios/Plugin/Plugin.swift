import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SquarePayment)
public class SquarePayment: CAPPlugin {

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""

        print("Echo Call!")
        print(value)

        let resolveSummaries = {
            call.success([
                "value": value
            ])
        }

        DispatchQueue.main.async {
            let view = ExampleViewController()
            self.bridge.viewController.present(view, animated: true, completion: resolveSummaries)
        }


    }
}
