import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        PlatformKoinInitializer.init().invoke()
        LogConfigKt.doInitNapierDebug()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
