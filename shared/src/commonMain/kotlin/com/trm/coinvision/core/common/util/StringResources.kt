package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

@Immutable
internal data class StringResources(
  val appTitle: String,
  val compare: String,
  val list: String,
  val portfolio: String,
  val errorOccurred: String,
  val retry: String,
  val searchForTokens: String,
  val loading: String,
  val swapTokens: String,
  val search: String,
  val back: String
)

internal fun stringResourcesEn() =
  StringResources(
    appTitle = "CoinVision",
    compare = "Compare",
    list = "List",
    portfolio = "Portfolio",
    errorOccurred = "Error occurred",
    retry = "Retry",
    searchForTokens = "Search for tokens...",
    loading = "Loading...",
    swapTokens = "Swap tokens",
    search = "Search",
    back = "Back",
  )

internal fun stringResourcesPl() =
  StringResources(
    appTitle = "CoinVision",
    compare = "Porównaj",
    list = "Lista",
    portfolio = "Portfolio",
    errorOccurred = "Wystąpił błąd",
    retry = "Ponów próbę",
    searchForTokens = "Szukaj tokenów...",
    loading = "Ładowanie...",
    swapTokens = "Zamień tokeny",
    search = "Szukaj",
    back = "Wstecz",
  )

internal val LocalStringResources = compositionLocalOf { stringResourcesEn() }

internal fun stringResources(language: String = Locale.current.language): StringResources =
  when (language) {
    "pl" -> stringResourcesPl()
    else -> stringResourcesEn()
  }
