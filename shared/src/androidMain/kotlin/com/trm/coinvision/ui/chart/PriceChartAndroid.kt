package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tradingview.lightweightcharts.view.ChartsView

@Composable
internal actual fun PriceChart(modifier: Modifier, points: List<PriceChartPoint>) {
  AndroidView(
    modifier = modifier,
    factory = { ChartsView(it) },
    update = {
      it.api.addAreaSeries { api -> api.setData(points.map(PriceChartPoint::toAreaData)) }
    }
  )
}
