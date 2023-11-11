package com.trm.coinvision.core.network.client

import com.trm.coinvision.core.network.model.CoinMarketsResponseItem
import com.trm.coinvision.core.network.model.CoinResponse
import com.trm.coinvision.core.network.model.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.test.runTest

class CoinGeckoApiClientTests {
  private lateinit var client: HttpClient

  @BeforeTest
  fun init() {
    client = coinGeckoHttpClient { coinGeckoHttpClientDefaultConfig(logLevel = LogLevel.ALL)() }
  }

  @Test
  fun search() = runTest {
    client
      .get(COIN_GECKO_API_BASE_URL) {
        url {
          appendPathSegments("search")
          parameters.append("query", "btc")
        }
      }
      .body<SearchResponse>()
      .run { assertFalse(coins.isNullOrEmpty()) }
  }

  @Test
  fun getCoinMarkets() = runTest {
    client
      .get(COIN_GECKO_API_BASE_URL) {
        url {
          appendPathSegments("coins", "markets")
          parameters.append("vs_currency", "usd")
        }
      }
      .body<List<CoinMarketsResponseItem>>()
      .run { assertFalse(isEmpty()) }
  }

  @Test
  fun getCoinById() = runTest {
    client
      .get(COIN_GECKO_API_BASE_URL) {
        url {
          appendPathSegments("coins", "bitcoin")
          parameters.append("localization", "false")
          parameters.append("tickers", "false")
          parameters.append("market_data", "true")
          parameters.append("community_data", "false")
          parameters.append("developer_data", "false")
          parameters.append("sparkline", "true")
        }
      }
      .body<CoinResponse>()
      .run { assertEquals("btc", symbol) }
  }
}
