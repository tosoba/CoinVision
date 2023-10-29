package com.trm.coinvision.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun coinGeckoApiClient(config: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(config)

fun coinGeckoApiClientDefaultConfig(logLevel: LogLevel? = null): HttpClientConfig<*>.() -> Unit = {
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

  logLevel?.let { install(Logging) { level = it } }

  install(HttpRequestRetry) {
    retryOnServerErrors(maxRetries = 3)
    exponentialDelay()
  }
}

internal const val COIN_GECKO_API_BASE_URL = "https://api.coingecko.com/api/v3"
