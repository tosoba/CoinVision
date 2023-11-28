package com.trm.coinvision.ui.chart

import com.trm.coinvision.core.domain.model.MarketChartDTO

internal fun MarketChartDTO.toPriceChartPoints(): List<PriceChartPoint> =
  prices
    ?.mapNotNull {
      if (it.size != 2) {
        null
      } else {
        val (timestamp, value) = it
        PriceChartPoint(timestamp.toLong(), value.toFloat())
      }
    }
    .orEmpty()
