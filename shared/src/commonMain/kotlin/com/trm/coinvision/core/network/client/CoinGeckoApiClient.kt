package com.trm.coinvision.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun coinGeckoApiClient(
  config: HttpClientConfig<*>.() -> Unit = coinGeckoApiClientDefaultConfig()
): HttpClient = HttpClient(config)

fun coinGeckoApiClientDefaultConfig(): HttpClientConfig<*>.() -> Unit = {
  install(ContentNegotiation) {
    json(
      Json {
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        ignoreUnknownKeys = true
      }
    )
  }
}

internal const val COIN_GECKO_API_BASE_URL = "https://api.coingecko.com/api/v3"
