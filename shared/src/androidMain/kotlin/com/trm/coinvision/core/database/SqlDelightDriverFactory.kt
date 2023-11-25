package com.trm.coinvision.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.trm.coinvision.db.CoinVisionDb

internal actual class DriverFactory(private val context: Context) {
  actual fun createDriver(): SqlDriver = AndroidSqliteDriver(CoinVisionDb.Schema, context, DB_NAME)
}
