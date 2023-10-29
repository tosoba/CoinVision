package com.trm.coinvision.core.data

import com.trm.coinvision.core.data.mapper.coinMarketsBody
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.network.client.coinGeckoApiClient
import com.trm.coinvision.core.network.client.coinGeckoApiClientDefaultConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.isSuccess
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class CryptoRepositoryTests {
  private lateinit var repository: CryptoNetworkRepository

  @BeforeTest
  fun init() {
    repository =
      CryptoNetworkRepository(
        coinGeckoApiClient {
          coinGeckoApiClientDefaultConfig()()
          install(Logging) { level = LogLevel.BODY }
        }
      )
  }

  @Test
  fun getCoinMarkets() = runTest {
    val response = repository.getCoinMarkets(FiatCurrency.USD)
    assertTrue(response.status.isSuccess())
    assertTrue(response.coinMarketsBody().isNotEmpty())
  }
}
