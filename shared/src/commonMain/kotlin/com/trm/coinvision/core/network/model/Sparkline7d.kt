package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class Sparkline7d(@SerialName("price") val price: List<Double>?)
