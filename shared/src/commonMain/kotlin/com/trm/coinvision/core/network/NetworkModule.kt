package com.trm.coinvision.core.network

import com.trm.coinvision.core.network.client.coinGeckoApiClient
import org.koin.dsl.module

internal val networkModule = module { single { coinGeckoApiClient() } }
