package com.trm.coinvision.core.cache

import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val cacheModule: Module = module {
  single {
    PlatformHttpCacheStorageInitializer(directory = androidContext().cacheDir.toOkioPath())(
      CACHE_SIZE
    )
  }
}
