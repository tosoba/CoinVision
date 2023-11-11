package com.trm.coinvision.core.network.model

import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CoinResponse(
  @SerialName("categories") override val categories: List<String>?,
  @SerialName("coingecko_rank") override val coingeckoRank: Int?,
  @SerialName("description") val _description: Description?,
  @SerialName("id") override val id: String?,
  @SerialName("image") override val image: Image?,
  @SerialName("last_updated") override val lastUpdated: String?,
  @SerialName("market_cap_rank") override val marketCapRank: Int?,
  @SerialName("market_data") override val marketData: MarketData?,
  @SerialName("name") override val name: String?,
  @SerialName("symbol") override val symbol: String?
) : TokenDTO {
  override val description: String?
    get() = _description?.en
}
