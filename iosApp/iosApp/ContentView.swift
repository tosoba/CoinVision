import shared
import SwiftUI
import UIKit

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context _: Context) -> UIViewController {
        MainViewControllerKt.mainViewController(composeSharedFactory: PriceChartUIViewFactory())
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}
