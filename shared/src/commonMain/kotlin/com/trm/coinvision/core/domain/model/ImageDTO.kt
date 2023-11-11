package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable

internal interface ImageDTO : Serializable {
  val large: String?
  val small: String?
  val thumb: String?
}
