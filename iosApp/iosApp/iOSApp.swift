import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        PlatformKoinInitializer.init().invoke()
		LogConfigKt.initNapierDebug()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
