package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable

internal data class TokenListItem(
  val currentPrice: Double,
  val high24h: Double?,
  val id: String,
  val image: String?,
  val low24h: Double?,
  val marketCap: Double?,
  val marketCapRank: Long?,
  val name: String,
  val priceChangePercentage24h: Double?,
  val symbol: String,
) : Serializable
