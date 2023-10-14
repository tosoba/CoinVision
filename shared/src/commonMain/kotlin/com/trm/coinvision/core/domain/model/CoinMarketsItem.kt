package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable

data class CoinMarketsItem(
  val currentPrice: Double,
  val high24h: Double?,
  val id: String,
  val low24h: Double?,
  val marketCap: Double?,
  val marketCapRank: Long?,
  val name: String,
  val priceChangePercentage24h: Double?,
  val symbol: String,
) : Serializable
