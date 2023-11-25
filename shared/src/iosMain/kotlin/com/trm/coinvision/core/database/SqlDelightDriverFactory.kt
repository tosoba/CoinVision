package com.trm.coinvision.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.trm.coinvision.db.CoinVisionDb

internal actual class DriverFactory {
  actual fun createDriver(): SqlDriver = NativeSqliteDriver(CoinVisionDb.Schema, DB_NAME)
}
