package com.trm.coinvision.core.database

import com.trm.coinvision.core.database.adapter.LocalDateTimeAdapter
import com.trm.coinvision.db.CoinVisionDb
import com.trm.coinvision.db.SelectedToken

internal class CoinVisionDatabase(databaseDriverFactory: DriverFactory) {
  private val database =
    CoinVisionDb(
      driver = databaseDriverFactory.createDriver(),
      selectedTokenAdapter = SelectedToken.Adapter(updatedAtAdapter = LocalDateTimeAdapter)
    )
  private val dbQueries = database.coinVisionDbQueries
}
