package com.trm.coinvision.core.data

import com.trm.coinvision.core.domain.repo.CryptoRepository
import org.koin.dsl.module

internal val dataModule = module { factory<CryptoRepository> { CryptoNetworkRepository(get()) } }
