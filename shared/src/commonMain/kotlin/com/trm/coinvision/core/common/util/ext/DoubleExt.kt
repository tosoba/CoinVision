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

fun Double.decimalFormat(significantDecimals: Int = 3, signed: Boolean = false): String =
  buildString {
    if (this@decimalFormat > 1) {
      if (signed) append("+")
      append(format(significantDecimals))
    } else {
      if (signed) {
        if (this@decimalFormat > 0) append("+") else if (this@decimalFormat < 0) append("-")
      }
      append("0.")
      var nonZeroFound = false
      var significant = 0
      BigDecimal.fromDouble(this@decimalFormat).toPlainString().substring(2).forEach {
        if (significant >= significantDecimals) return@forEach
        nonZeroFound = nonZeroFound || it != '0'
        if (nonZeroFound) {
          ++significant
          append(it)
        }
      }
      for (index in this.lastIndex downTo if (signed) 4 else 3) {
        if (this[index] != '0') break
        deleteAt(index)
      }
    }
  }
