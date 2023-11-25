package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tradingview.lightweightcharts.api.series.models.AreaData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.view.ChartsView

@Composable
internal actual fun PriceChart(modifier: Modifier) {
  AndroidView(
    modifier = modifier,
    factory = { ChartsView(it) },
    update = {
      it.api.addAreaSeries { api ->
        api.setData(
          listOf(
            AreaData(time = Time.BusinessDay(2023, 10, 25), value = 1f),
            AreaData(time = Time.BusinessDay(2023, 11, 1), value = 2f),
            AreaData(time = Time.BusinessDay(2023, 11, 8), value = 3f),
            AreaData(time = Time.BusinessDay(2023, 11, 15), value = 4f),
            AreaData(time = Time.BusinessDay(2023, 11, 22), value = 5f),
          )
        )
      }
    }
  )
}
