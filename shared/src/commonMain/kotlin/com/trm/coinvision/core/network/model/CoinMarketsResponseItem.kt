package com.trm.coinvision.core.network.model

import com.trm.coinvision.core.domain.model.TokenListItemDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CoinMarketsResponseItem(
  @SerialName("circulating_supply") override val circulatingSupply: Double?,
  @SerialName("current_price") val _currentPrice: Double?,
  @SerialName("fully_diluted_valuation") override val fullyDilutedValuation: Double?,
  @SerialName("high_24h") override val high24h: Double?,
  @SerialName("id") val _id: String?,
  @SerialName("image") override val image: String?,
  @SerialName("last_updated") override val lastUpdated: String?,
  @SerialName("low_24h") override val low24h: Double?,
  @SerialName("market_cap") override val marketCap: Double?,
  @SerialName("market_cap_change_24h") override val marketCapChange24h: Double?,
  @SerialName("market_cap_change_percentage_24h")
  override val marketCapChangePercentage24h: Double?,
  @SerialName("market_cap_rank") override val marketCapRank: Long?,
  @SerialName("max_supply") override val maxSupply: Double?,
  @SerialName("name") val _name: String?,
  @SerialName("price_change_24h") override val priceChange24h: Double?,
  @SerialName("price_change_percentage_24h") override val priceChangePercentage24h: Double?,
  @SerialName("symbol") val _symbol: String?,
  @SerialName("total_supply") override val totalSupply: Double?,
  @SerialName("total_volume") override val totalVolume: Double?
) : TokenListItemDTO {
  override val currentPrice: Double
    get() = requireNotNull(_currentPrice)

  override val id: String
    get() = requireNotNull(_id)

  override val name: String
    get() = requireNotNull(_name)

  override val symbol: String
    get() = requireNotNull(_symbol)
}
