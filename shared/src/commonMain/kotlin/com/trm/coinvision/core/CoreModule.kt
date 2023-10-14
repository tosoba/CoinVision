package com.trm.coinvision.core

import com.trm.coinvision.core.data.dataModule
import com.trm.coinvision.core.network.networkModule
import org.koin.dsl.module

val coreModule = module { includes(networkModule, dataModule) }
