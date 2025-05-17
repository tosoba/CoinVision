import UIKit
import shared

class PriceChartUIViewFactory: ComposeSharedFactory {
    func createPriceChartUIView(points: [PriceChartPoint]) -> KotlinPair<UIView, any PriceChartUIViewDelegate> {
        let view = PriceChartUIView(points: points)
        return KotlinPair(first: view, second: view)
    }
}

class PriceChartUIView : UIView, PriceChartUIViewDelegate {
    private var chart: LightweightCharts!
    
    init(points: [PriceChartPoint], frame: CGRect = .zero) {
        super.init(frame: frame)
        setupChartView(points: points)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupChartView(points: [PriceChartPoint]) {
        let options = ChartOptions()
        chart = LightweightCharts(options: options)
        chart.translatesAutoresizingMaskIntoConstraints = false
        let series = chart.addBaselineSeries(
            options: BaselineSeriesOptions(
                topFillColor1: "#fff",
                topFillColor2: "#0f0"
            ))
        // series.setData(data: <#T##[BaselineData]#>)
    }
    
    func updatePoints(points: [PriceChartPoint]) {
        
    }
}
