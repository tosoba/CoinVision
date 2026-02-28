import shared
import SwiftUI
import UIKit

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context _: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(generatedViewFactory: PriceChartUIViewFactory())
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}
