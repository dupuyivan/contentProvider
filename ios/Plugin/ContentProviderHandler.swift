import Foundation

@objc public class ContentProviderHandler: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
