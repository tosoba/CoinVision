package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Coin(
  @SerialName("api_symbol") val apiSymbol: String?,
  @SerialName("id") val id: String?,
  @SerialName("large") val large: String?,
  @SerialName("market_cap_rank") val marketCapRank: Int?,
  @SerialName("name") val name: String?,
  @SerialName("symbol") val symbol: String?,
  @SerialName("thumb") val thumb: String?
)
