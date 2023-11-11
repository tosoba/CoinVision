package com.trm.coinvision.core.network.model

import com.trm.coinvision.core.domain.model.ImageDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Image(
  @SerialName("large") override val large: String?,
  @SerialName("small") override val small: String?,
  @SerialName("thumb") override val thumb: String?
) : ImageDTO
