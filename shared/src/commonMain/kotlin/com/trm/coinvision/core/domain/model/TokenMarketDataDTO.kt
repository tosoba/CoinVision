package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable

internal interface TokenMarketDataDTO : Serializable {
  val circulatingSupply: Double?
  val currentPrice: ValueInCurrencyDTO?
  val fullyDilutedValuation: ValueInCurrencyDTO?
  val high24h: ValueInCurrencyDTO?
  val lastUpdated: String?
  val low24h: ValueInCurrencyDTO?
  val marketCap: ValueInCurrencyDTO?
  val marketCapChange24h: Double?
  val marketCapChange24hInCurrency: ValueInCurrencyDTO?
  val marketCapChangePercentage24h: Double?
  val marketCapChangePercentage24hInCurrency: ValueInCurrencyDTO?
  val marketCapFdvRatio: Double?
  val marketCapRank: Int?
  val maxSupply: Double?
  val priceChange24h: Double?
  val priceChange24hInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage14d: Double?
  val priceChangePercentage14dInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage1hInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage1y: Double?
  val priceChangePercentage1yInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage200d: Double?
  val priceChangePercentage200dInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage24h: Double?
  val priceChangePercentage24hInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage30d: Double?
  val priceChangePercentage30dInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage60d: Double?
  val priceChangePercentage60dInCurrency: ValueInCurrencyDTO?
  val priceChangePercentage7d: Double?
  val priceChangePercentage7dInCurrency: ValueInCurrencyDTO?
  val priceHistory: List<Double>?
  val totalSupply: Double?
}
