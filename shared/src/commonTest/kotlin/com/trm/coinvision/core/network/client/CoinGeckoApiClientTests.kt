package com.trm.coinvision.core.network.client

import com.trm.coinvision.core.network.model.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlinx.coroutines.test.runTest

class CoinGeckoApiClientTests {
  private lateinit var client: HttpClient

  @BeforeTest
  fun init() {
    client = coinGeckoApiClient { install(Logging) { level = LogLevel.BODY } }
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
}
