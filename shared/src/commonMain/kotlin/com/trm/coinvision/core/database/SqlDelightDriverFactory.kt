package com.trm.coinvision.core.database

import app.cash.sqldelight.db.SqlDriver

internal expect class DriverFactory {
  fun createDriver(): SqlDriver
}

internal const val DB_NAME = "CoinVision.db"
