package com.trm.coinvision.core.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

internal actual val databaseModule = module {
  single { CoinVisionDatabase(DriverFactory(), Dispatchers.IO) }
}
