package com.trm.coinvision.ui.chart

import com.tradingview.lightweightcharts.api.series.models.AreaData
import com.tradingview.lightweightcharts.api.series.models.Time
import java.util.Date

internal fun PriceChartPoint.toAreaData(): AreaData =
  AreaData(time = Time.Utc.fromDate(Date(timestamp)), value = value)
