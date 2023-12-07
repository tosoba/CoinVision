package com.trm.coinvision.core.common.util.ext

import kotlin.math.pow
import kotlin.math.round

fun Double.toMarketCapFormat(): String {
  val trillion = 1_000_000_000_000.0
  val billion = 1_000_000_000.0
  val million = 1_000_000.0
  val thousand = 1_000.0
  return when {
    this >= trillion -> "${(this / trillion).format(1)}T"
    this >= billion -> "${(this / billion).format(1)}B"
    this >= million -> "${(this / million).format(1)}M"
    this >= thousand -> "${(this / thousand).format(1)}K"
    else -> format(1)
  }
}

fun Double.format(digits: Int): String {
  val multiplier = 10.0.pow(digits.toDouble())
  val rounded = round(this * multiplier) / multiplier
  return rounded.toString()
}
