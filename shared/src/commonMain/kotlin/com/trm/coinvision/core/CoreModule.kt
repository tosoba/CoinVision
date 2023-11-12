package com.trm.coinvision.core

import com.trm.coinvision.core.cache.cacheModule
import com.trm.coinvision.core.data.dataModule
import com.trm.coinvision.core.datastore.datastoreModule
import com.trm.coinvision.core.domain.domainModule
import com.trm.coinvision.core.network.networkModule
import org.koin.dsl.module

internal val coreModule = module {
  includes(
    cacheModule,
    dataModule,
    datastoreModule,
    domainModule,
    networkModule,
  )
}
