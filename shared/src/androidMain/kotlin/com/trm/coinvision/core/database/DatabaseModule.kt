package com.trm.coinvision.core.database

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal actual val databaseModule = module {
  single { CoinVisionDatabase(DriverFactory(androidContext())) }
}
