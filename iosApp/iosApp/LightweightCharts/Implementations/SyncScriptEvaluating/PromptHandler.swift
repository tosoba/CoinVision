import Foundation
import WebKit

protocol ClosuresStore: AnyObject {
    func addMethod<Input, Output>(_ method: JavaScriptMethod<Input, Output>, forName name: String)
}

// MARK: -

class PromptHandler: NSObject, ClosuresStore {
    private let decoder = JSONDecoder()

    private var closures: [String: JavaScriptSyncMethod] = [:]

    func addMethod<Input: Decodable, Output>(_ method: JavaScriptMethod<Input, Output>, forName name: String) {
        switch method {
        case .closure:
            closures[name] = method
        case .javaScript:
            break
        }
    }
}

// MARK: - WKUIDelegate

extension PromptHandler: WKUIDelegate {
    struct Payload: Decodable {
        let object: String
    }

    func webView(
        _: WKWebView,
        runJavaScriptTextInputPanelWithPrompt prompt: String,
        defaultText: String?,
        initiatedByFrame _: WKFrameInfo,
        completionHandler: @escaping (String?) -> Void
    ) {
        if let payloadData = prompt.data(using: .utf8, allowLossyConversion: false),
           let payload = try? decoder.decode(Payload.self, from: payloadData)
        {
            let result = closures[payload.object]?.evaluate(payloadData: payloadData, with: decoder)
            completionHandler(result)
        } else {
            completionHandler(defaultText)
        }
    }
}
