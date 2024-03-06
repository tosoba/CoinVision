package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

@Immutable
internal data class StringResources(
  val compare: String,
  val list: String,
  val errorOccurred: String,
  val retry: String,
  val searchForTokens: String,
  val loading: String,
  val swapTokens: String,
  val search: String,
  val back: String,
  val exceededRateLimit: String,
  val apiErrorOccurred: String,
  val noInternetConnection: String,
  val `if`: String,
  val reachedMarketCapOf: String,
  val hadMarketCapOf: String,
  val wouldBeWorth: String,
  val tokenComparisonDataIncomplete: String,
)

internal fun stringResourcesEn() =
  StringResources(
    compare = "Compare",
    list = "List",
    errorOccurred = "Error occurred",
    retry = "Retry",
    searchForTokens = "Search for tokens…",
    loading = "Loading…",
    swapTokens = "Swap tokens",
    search = "Search",
    back = "Back",
    exceededRateLimit = "Exceeded request rate limit. Try again later.",
    apiErrorOccurred = "API error occurred.",
    noInternetConnection = "No internet connection.",
    `if` = "If",
    reachedMarketCapOf = "reached the market cap of",
    hadMarketCapOf = "had the market cap of",
    wouldBeWorth = "would be worth",
    tokenComparisonDataIncomplete =
      "Data retrieved for at least one of selected tokens is incomplete - choose a different token pair.",
  )

internal fun stringResourcesPl() =
  StringResources(
    compare = "Porównaj",
    list = "Lista",
    errorOccurred = "Wystąpił błąd",
    retry = "Ponów próbę",
    searchForTokens = "Szukaj tokenów…",
    loading = "Ładowanie…",
    swapTokens = "Zamień tokeny",
    search = "Szukaj",
    back = "Wstecz",
    exceededRateLimit = "Przekroczono limit API. Spróbuj później.",
    apiErrorOccurred = "Wystąpił błąd API.",
    noInternetConnection = "Brak połączenia z internetem.",
    `if` = "Jeśli",
    reachedMarketCapOf = "osiągnie kapitalizację rynkową",
    hadMarketCapOf = "miało kapitalizację rynkową",
    wouldBeWorth = "byłoby warte",
    tokenComparisonDataIncomplete =
      "Dane pobrane dla przynajmniej jednego z wybranych tokenów są niekompletne - wybierz inną parę tokenów.",
  )

internal val LocalStringResources = compositionLocalOf { stringResourcesEn() }

internal fun stringResources(language: String = Locale.current.language): StringResources =
  when (language) {
    "pl" -> stringResourcesPl()
    else -> stringResourcesEn()
  }
