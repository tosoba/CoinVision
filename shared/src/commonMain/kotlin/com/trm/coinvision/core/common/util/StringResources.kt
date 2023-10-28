package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.intl.Locale

@Immutable data class StringResources(val appTitle: String, val compare: String, val list: String)

fun stringResourcesEn() =
  StringResources(appTitle = "CoinVision", compare = "Compare", list = "List")

fun stringResourcesPl() =
  StringResources(appTitle = "CoinVision", compare = "Porównaj", list = "Lista")

val LocalStringResources = staticCompositionLocalOf { stringResourcesEn() }

fun stringResources(): StringResources =
  when (Locale.current.language) {
    "PL" -> stringResourcesPl()
    else -> stringResourcesEn()
  }
