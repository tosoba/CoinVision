package com.trm.coinvision.core.network

import coingecko.CoinGeckoClient
import com.trm.coinvision.core.network.util.testCoinGeckoClient
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class CoinGeckoClientTests {
  private lateinit var coinGeckoClient: CoinGeckoClient

  @BeforeTest
  fun init() {
    coinGeckoClient = testCoinGeckoClient()
  }

  @Test fun ping() = runTest { coinGeckoClient.ping() }
}
