import shared
import SwiftUI
import UIKit

@main
struct iOSApp: App {
    init() {
        PlatformKoinInitializer().invoke()
        LogConfigKt.doInitNapierDebug()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

private struct ContentView: View {
    var body: some View {
        TabView {
            ComposeRouteView(route: .compareTokens)
                .tabItem {
                    Label("Compare", systemImage: "arrow.left.arrow.right")
                }

            ComposeRouteView(route: .tokensList)
                .tabItem {
                    Label("List", systemImage: "list.bullet")
                }
                .ignoresSafeArea(edges: .bottom)
        }
        .tint(.indigo)
    }
}

private struct ComposeRouteView: UIViewControllerRepresentable {
    let route: Route

    func makeUIViewController(context _: Context) -> UIViewController {
        let composeSharedFactory = PriceChartUIViewFactory()

        switch route {
        case .compareTokens:
            return RouteViewControllersKt.compareTokensViewController(
                composeSharedFactory: composeSharedFactory
            )
        case .tokensList:
            return RouteViewControllersKt.tokensListViewController(
                composeSharedFactory: composeSharedFactory
            )
        }
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}

    enum Route {
        case compareTokens
        case tokensList
    }
}
