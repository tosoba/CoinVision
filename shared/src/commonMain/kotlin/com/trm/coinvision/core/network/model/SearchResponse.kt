package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchResponse(
  @SerialName("categories") val categories: List<Category>?,
  @SerialName("coins") val coins: List<Coin>?,
  @SerialName("exchanges") val exchanges: List<Exchange>?,
  @SerialName("nfts") val nfts: List<Nft>?
)
