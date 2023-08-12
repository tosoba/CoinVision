package com.trm.coinvision

import coingecko.CoinGeckoClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class CoinGeckoClientTests {
  private lateinit var coinGeckoClient: CoinGeckoClient

  @BeforeTest
  fun initializeClient() {
    coinGeckoClient = CoinGeckoClient(HttpClient { install(Logging) { level = LogLevel.BODY } })
  }

  @Test fun ping() = runTest { coinGeckoClient.ping() }
}
