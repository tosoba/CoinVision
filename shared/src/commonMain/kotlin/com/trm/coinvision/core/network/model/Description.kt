package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class Description(@SerialName("en") val en: String?)
