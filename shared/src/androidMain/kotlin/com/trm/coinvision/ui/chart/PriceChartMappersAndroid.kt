package com.trm.coinvision.ui.chart

import com.tradingview.lightweightcharts.api.series.models.AreaData
import com.tradingview.lightweightcharts.api.series.models.Time

internal fun PriceChartPoint.toAreaData() = AreaData(time = Time.Utc(timestamp), value = value)
