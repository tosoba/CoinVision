package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Nft(
  @SerialName("id") val id: String?,
  @SerialName("name") val name: String?,
  @SerialName("symbol") val symbol: String?,
  @SerialName("thumb") val thumb: String?
)
