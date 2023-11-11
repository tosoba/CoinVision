package com.trm.coinvision.core.network

import com.trm.coinvision.core.network.client.CoinGeckoApiClient
import com.trm.coinvision.core.network.client.coinGeckoHttpClient
import com.trm.coinvision.core.network.client.coinGeckoHttpClientDefaultConfig
import io.ktor.client.plugins.logging.LogLevel
import org.koin.dsl.module

internal val networkModule = module {
  single {
    coinGeckoHttpClient {
      coinGeckoHttpClientDefaultConfig(logLevel = LogLevel.ALL, cacheStorage = get())()
    }
  }
  factory { CoinGeckoApiClient(get()) }
}
