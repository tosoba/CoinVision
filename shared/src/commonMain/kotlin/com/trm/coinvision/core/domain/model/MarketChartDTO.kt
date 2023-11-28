package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable

interface MarketChartDTO : Serializable {
  val marketCaps: List<List<Double>>?
  val prices: List<List<Double>>?
  val totalVolumes: List<List<Double>>?
}
