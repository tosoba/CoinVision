package com.trm.coinvision.core.network.model

import com.trm.coinvision.core.domain.model.MarketChartDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketChartResponse(
  @SerialName("market_caps") override val marketCaps: List<List<Double>>?,
  @SerialName("prices") override val prices: List<List<Double>>?,
  @SerialName("total_volumes") override val totalVolumes: List<List<Double>>?
) : MarketChartDTO
