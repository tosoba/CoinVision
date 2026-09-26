package com.trm.coinvision.core.domain.model

internal interface TokenDTO {
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
