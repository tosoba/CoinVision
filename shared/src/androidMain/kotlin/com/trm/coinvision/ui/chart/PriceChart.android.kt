package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tradingview.lightweightcharts.api.series.models.AreaData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.view.ChartsView
import java.util.Date

@Composable
internal actual fun PriceChart(modifier: Modifier, points: List<PriceChartPoint>) {
  AndroidView(
    modifier = modifier,
    factory = ::ChartsView,
    update = {
      it.api.addAreaSeries { api -> api.setData(points.map(PriceChartPoint::toAreaData)) }
    },
  )
}

private fun PriceChartPoint.toAreaData(): AreaData =
  AreaData(time = Time.Utc.fromDate(Date(timestamp)), value = value)

@Composable
internal actual fun PriceChartUIView(modifier: Modifier, points: List<PriceChartPoint>) {
  throw NotImplementedError("Use PriceChart instead.")
}

actual interface ComposeSharedFactory
