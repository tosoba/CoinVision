package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable

internal interface TokenDTO : Serializable {
  val categories: List<String>?
  val coingeckoRank: Int?
  val description: String?
  val id: String?
  val image: ImageDTO?
  val lastUpdated: String?
  val marketCapRank: Int?
  val marketData: TokenMarketDataDTO?
  val name: String?
  val symbol: String?
}
