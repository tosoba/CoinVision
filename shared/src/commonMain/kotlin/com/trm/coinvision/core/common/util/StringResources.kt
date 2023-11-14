package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

@Immutable
internal data class StringResources(
  val appTitle: String,
  val compare: String,
  val list: String,
  val errorOccurred: String,
  val retry: String,
  val searchForTokens: String,
  val loading: String
)

internal fun stringResourcesEn() =
  StringResources(
    appTitle = "CoinVision",
    compare = "Compare",
    list = "List",
    errorOccurred = "Error occurred",
    retry = "Retry",
    searchForTokens = "Search for tokens...",
    loading = "Loading..."
  )

internal fun stringResourcesPl() =
  StringResources(
    appTitle = "CoinVision",
    compare = "Porównaj",
    list = "Lista",
    errorOccurred = "Wystąpił błąd",
    retry = "Ponów próbę",
    searchForTokens = "Szukaj tokenów...",
    loading = "Ładowanie..."
  )

internal val LocalStringResources = compositionLocalOf { stringResourcesEn() }

internal fun stringResources(language: String = Locale.current.language): StringResources =
  when (language) {
    "pl" -> stringResourcesPl()
    else -> stringResourcesEn()
  }
