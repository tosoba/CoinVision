package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Exchange(
  @SerialName("id") val id: String?,
  @SerialName("large") val large: String?,
  @SerialName("market_type") val marketType: String?,
  @SerialName("name") val name: String?,
  @SerialName("thumb") val thumb: String?
)
