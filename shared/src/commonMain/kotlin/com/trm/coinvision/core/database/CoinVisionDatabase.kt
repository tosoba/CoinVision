package com.trm.coinvision.core.database

import com.trm.coinvision.core.database.adapter.LocalDateTimeAdapter
import com.trm.coinvision.db.CoinVisionDb
import com.trm.coinvision.db.MainToken
import com.trm.coinvision.db.ReferenceToken

internal class CoinVisionDatabase(databaseDriverFactory: DriverFactory) {
  private val database =
    CoinVisionDb(
      driver = databaseDriverFactory.createDriver(),
      mainTokenAdapter = MainToken.Adapter(updatedAtAdapter = LocalDateTimeAdapter),
      referenceTokenAdapter = ReferenceToken.Adapter(updatedAtAdapter = LocalDateTimeAdapter),
    )
  private val dbQueries = database.coinVisionDbQueries
}
