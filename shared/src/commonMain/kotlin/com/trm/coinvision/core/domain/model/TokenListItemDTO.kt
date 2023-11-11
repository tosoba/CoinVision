package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable

internal interface TokenListItemDTO : Serializable {
  val circulatingSupply: Double?
  val currentPrice: Double
  val fullyDilutedValuation: Double?
  val high24h: Double?
  val id: String
  val image: String?
  val lastUpdated: String?
  val low24h: Double?
  val marketCap: Double?
  val marketCapChange24h: Double?
  val marketCapChangePercentage24h: Double?
  val marketCapRank: Long?
  val maxSupply: Double?
  val name: String
  val priceChange24h: Double?
  val priceChangePercentage24h: Double?
  val symbol: String
  val totalSupply: Double?
  val totalVolume: Double?
}
