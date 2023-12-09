package com.trm.coinvision.core.common.util.ext

import com.ionspin.kotlin.bignum.decimal.BigDecimal
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

fun Double.formatPrice(significantDecimals: Int = 3): String =
  if (this > 1) {
    format(2)
  } else {
    buildString {
      append("0.")
      var nonZeroFound = false
      var significant = 0
      BigDecimal.fromDouble(this@formatPrice).toPlainString().substring(2).forEach {
        if (significant >= significantDecimals) return@forEach
        nonZeroFound = nonZeroFound || it != '0'
        if (nonZeroFound) {
          ++significant
          append(it)
        }
      }
      for (index in this.lastIndex downTo 3) {
        if (this[index] != '0') break
        deleteAt(index)
      }
    }
  }
