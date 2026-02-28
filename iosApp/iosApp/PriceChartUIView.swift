import shared
import UIKit

class PriceChartUIViewFactory: ComposeSharedFactory {
    func createPriceChartUIView(points: [PriceChartPoint]) -> KotlinPair<UIView, any PriceChartUIViewDelegate> {
        let view = PriceChartUIView(points: points)
        return KotlinPair(first: view, second: view)
    }
}

class PriceChartUIView: UIView, PriceChartUIViewDelegate {
    private var chart: LightweightCharts!
    private var series: AreaSeries!

    init(points: [PriceChartPoint], frame: CGRect = .zero) {
        super.init(frame: frame)
        setupChartView(points: points)
    }

    @available(*, unavailable)
    required init?(coder _: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func setupChartView(points: [PriceChartPoint]) {
        let options = ChartOptions()
        chart = LightweightCharts(options: options)
        chart.translatesAutoresizingMaskIntoConstraints = false
        addSubview(chart)
        NSLayoutConstraint.activate([
            chart.topAnchor.constraint(equalTo: topAnchor),
            chart.bottomAnchor.constraint(equalTo: bottomAnchor),
            chart.leadingAnchor.constraint(equalTo: leadingAnchor),
            chart.trailingAnchor.constraint(equalTo: trailingAnchor),
        ])

        series = chart.addAreaSeries(options: AreaSeriesOptions())
        updatePoints(points: points)
    }

    func updatePoints(points: [PriceChartPoint]) {
        series.setData(data: points.map { AreaData(time: .utc(timestamp: Double($0.timestamp)), value: Double($0.value)) })
    }
}
