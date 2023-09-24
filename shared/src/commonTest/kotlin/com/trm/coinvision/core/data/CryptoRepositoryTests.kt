package com.trm.coinvision.core.data

import com.trm.coinvision.core.network.util.testCoinGeckoClient
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class CryptoRepositoryTests {
  private lateinit var repository: CryptoRepository

  @BeforeTest
  fun init() {
    repository = CryptoRepository(testCoinGeckoClient())
  }

  @Test fun getCoinMarkets() = runTest { repository.getCoinMarkets("usd") }
}
