package com.trm.coinvision.ui.chart

import com.trm.coinvision.core.domain.model.MarketChartDTO

internal fun MarketChartDTO.toPriceChartPoints(): List<PriceChartPoint> =
  prices
    ?.mapNotNull { pricePoint ->
      if (pricePoint.size != 2 || pricePoint.any { it == null }) {
        null
      } else {
        val (timestamp, value) = pricePoint
        PriceChartPoint(requireNotNull(timestamp).toLong(), requireNotNull(value).toFloat())
      }
    }
    .orEmpty()
