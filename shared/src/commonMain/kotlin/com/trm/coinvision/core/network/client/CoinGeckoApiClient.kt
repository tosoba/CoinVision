package com.trm.coinvision.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun coinGeckoApiClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient = HttpClient {
  expectSuccess = true
  install(ContentNegotiation) {
    json(
      Json {
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
        ignoreUnknownKeys = true
      }
    )
  }
  config()
}

internal const val COIN_GECKO_API_BASE_URL = "https://api.coingecko.com/api/v3"
