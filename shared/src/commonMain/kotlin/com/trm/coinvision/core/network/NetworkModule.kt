package com.trm.coinvision.core.network

import com.trm.coinvision.core.network.client.coinGeckoApiClient
import com.trm.coinvision.core.network.client.coinGeckoApiClientDefaultConfig
import io.ktor.client.plugins.logging.LogLevel
import org.koin.dsl.module

internal val networkModule = module {
  single {
    coinGeckoApiClient {
      coinGeckoApiClientDefaultConfig(logLevel = LogLevel.ALL, cacheStorage = get())()
    }
  }
}
