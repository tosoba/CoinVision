import SwiftUI

@main
struct iOSApp: App {
    init() {
        PlatformKoinInitializer()()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
