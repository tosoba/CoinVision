package com.trm.coinvision.core.domain.model

interface MarketChartDTO {
  val marketCaps: List<List<Double?>>?
  val prices: List<List<Double?>>?
  val totalVolumes: List<List<Double?>>?
}
