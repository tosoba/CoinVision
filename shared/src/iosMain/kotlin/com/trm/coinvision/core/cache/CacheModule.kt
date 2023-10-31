package com.trm.coinvision.core.cache

import org.koin.core.module.Module
import org.koin.dsl.module

actual val cacheModule: Module = module {
  single { PlatformHttpCacheStorageInitializer()(CACHE_SIZE) }
}
