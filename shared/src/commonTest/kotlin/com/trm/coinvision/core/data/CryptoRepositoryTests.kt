package com.trm.coinvision.core.data

import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.network.client.CoinGeckoApiClient
import com.trm.coinvision.core.network.client.coinGeckoHttpClient
import com.trm.coinvision.core.network.client.coinGeckoHttpClientDefaultConfig
import com.trm.coinvision.core.network.mapper.toTokenListItems
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.http.isSuccess
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class CryptoRepositoryTests {
  private lateinit var repository: CoinGeckoApiClient

  @BeforeTest
  fun init() {
    repository =
      CoinGeckoApiClient(
        coinGeckoHttpClient { coinGeckoHttpClientDefaultConfig(logLevel = LogLevel.ALL)() }
      )
  }

  @Test
  fun getCoinMarkets() = runTest {
    val response = repository.getTokens(FiatCurrency.USD)
    assertTrue(response.status.isSuccess())
    assertTrue(response.toTokenListItems().isNotEmpty())
  }

  @Test
  fun getMarketChart() = runTest {
    val response = repository.getMarketChart("bitcoin", FiatCurrency.USD)
    assertTrue(response.status.isSuccess())
  }
}
