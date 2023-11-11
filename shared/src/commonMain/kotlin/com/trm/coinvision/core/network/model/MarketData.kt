package com.trm.coinvision.core.network.model

import com.trm.coinvision.core.domain.model.TokenMarketDataDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MarketData(
  @SerialName("circulating_supply") override val circulatingSupply: Double?,
  @SerialName("current_price") override val currentPrice: ValueInCurrency?,
  @SerialName("fully_diluted_valuation")
  override val fullyDilutedValuation: ValueInCurrency?,
  @SerialName("high_24h") override val high24h: ValueInCurrency?,
  @SerialName("last_updated") override val lastUpdated: String?,
  @SerialName("low_24h") override val low24h: ValueInCurrency?,
  @SerialName("market_cap") override val marketCap: ValueInCurrency?,
  @SerialName("market_cap_change_24h") override val marketCapChange24h: Double?,
  @SerialName("market_cap_change_24h_in_currency")
  override val marketCapChange24hInCurrency: ValueInCurrency?,
  @SerialName("market_cap_change_percentage_24h")
  override val marketCapChangePercentage24h: Double?,
  @SerialName("market_cap_change_percentage_24h_in_currency")
  override val marketCapChangePercentage24hInCurrency: ValueInCurrency?,
  @SerialName("market_cap_fdv_ratio") override val marketCapFdvRatio: Double?,
  @SerialName("market_cap_rank") override val marketCapRank: Int?,
  @SerialName("max_supply") override val maxSupply: Double?,
  @SerialName("price_change_24h") override val priceChange24h: Double?,
  @SerialName("price_change_24h_in_currency")
  override val priceChange24hInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_14d") override val priceChangePercentage14d: Double?,
  @SerialName("price_change_percentage_14d_in_currency")
  override val priceChangePercentage14dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_1h_in_currency")
  override val priceChangePercentage1hInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_1y") override val priceChangePercentage1y: Double?,
  @SerialName("price_change_percentage_1y_in_currency")
  override val priceChangePercentage1yInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_200d") override val priceChangePercentage200d: Double?,
  @SerialName("price_change_percentage_200d_in_currency")
  override val priceChangePercentage200dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_24h") override val priceChangePercentage24h: Double?,
  @SerialName("price_change_percentage_24h_in_currency")
  override val priceChangePercentage24hInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_30d") override val priceChangePercentage30d: Double?,
  @SerialName("price_change_percentage_30d_in_currency")
  override val priceChangePercentage30dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_60d") override val priceChangePercentage60d: Double?,
  @SerialName("price_change_percentage_60d_in_currency")
  override val priceChangePercentage60dInCurrency: ValueInCurrency?,
  @SerialName("price_change_percentage_7d") override val priceChangePercentage7d: Double?,
  @SerialName("price_change_percentage_7d_in_currency")
  override val priceChangePercentage7dInCurrency: ValueInCurrency?,
  @SerialName("sparkline_7d") val sparkline7d: Sparkline?,
  @SerialName("total_supply") override val totalSupply: Double?,
) : TokenMarketDataDTO {
  override val priceHistory: List<Double>?
    get() = sparkline7d?.price
}
