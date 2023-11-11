package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketData(
  @SerialName("circulating_supply") val circulatingSupply: Double?,
  @SerialName("current_price") val currentPrice: ValueInCurrency?,
  @SerialName("fully_diluted_valuation") val fullyDilutedValuation: ValueInCurrency?,
  @SerialName("high_24h") val high24h: ValueInCurrency?,
  @SerialName("last_updated") val lastUpdated: String?,
  @SerialName("low_24h") val low24h: ValueInCurrency?,
  @SerialName("market_cap") val marketCap: ValueInCurrency?,
  @SerialName("market_cap_change_24h") val marketCapChange24h: Double?,
  @SerialName("market_cap_change_24h_in_currency")
  val marketCapChange24hInCurrency: ValueInCurrency?,
  @SerialName("market_cap_change_percentage_24h") val marketCapChangePercentage24h: Double?,
  @SerialName("market_cap_change_percentage_24h_in_currency")
  val marketCapChangePercentage24hInCurrency: ValueInCurrency?,
  @SerialName("market_cap_fdv_ratio") val marketCapFdvRatio: Double?,
  @SerialName("market_cap_rank") val marketCapRank: Int?,
  @SerialName("max_supply") val maxSupply: Double?,
  @SerialName("price_change_24h") val priceChange24h: Double?,
  @SerialName("price_change_24h_in_currency") val priceChange24hInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_14d") val priceChangePercentage14d: Double?,
  @SerialName("price_change_percentage_14d_in_currency")
  val priceChangePercentage14dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_1h_in_currency")
  val priceChangePercentage1hInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_1y") val priceChangePercentage1y: Double?,
  @SerialName("price_change_percentage_1y_in_currency")
  val priceChangePercentage1yInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_200d") val priceChangePercentage200d: Double?,
  @SerialName("price_change_percentage_200d_in_currency")
  val priceChangePercentage200dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
  @SerialName("price_change_percentage_24h_in_currency")
  val priceChangePercentage24hInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_30d") val priceChangePercentage30d: Double?,
  @SerialName("price_change_percentage_30d_in_currency")
  val priceChangePercentage30dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_60d") val priceChangePercentage60d: Double?,
  @SerialName("price_change_percentage_60d_in_currency")
  val priceChangePercentage60dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_7d") val priceChangePercentage7d: Double?,
  @SerialName("price_change_percentage_7d_in_currency")
  val priceChangePercentage7dInCurrency: ValueInCurrency?,
  @SerialName("sparkline_7d") val sparkline7d: Sparkline7d?,
  @SerialName("total_supply") val totalSupply: Double?,
)
