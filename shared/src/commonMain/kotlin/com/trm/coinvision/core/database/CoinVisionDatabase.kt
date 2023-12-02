package com.trm.coinvision.core.database

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.trm.coinvision.core.database.adapter.LocalDateTimeAdapter
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.db.ChartPeriod
import com.trm.coinvision.db.CoinVisionDb
import com.trm.coinvision.db.MainToken
import com.trm.coinvision.db.ReferenceToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class CoinVisionDatabase(
  databaseDriverFactory: DriverFactory,
  private val dispatcher: CoroutineDispatcher
) {
  private val database: CoinVisionDb =
    CoinVisionDb(
      driver = databaseDriverFactory.createDriver(),
      mainTokenAdapter = MainToken.Adapter(LocalDateTimeAdapter),
      referenceTokenAdapter = ReferenceToken.Adapter(LocalDateTimeAdapter),
      chartPeriodAdapter = ChartPeriod.Adapter(EnumColumnAdapter())
    )
  private val dbQueries = database.coinVisionDbQueries

  suspend fun insertSelectedMainToken(token: SelectedToken) {
    withContext(dispatcher) {
      dbQueries.insertMainToken(
        id = token.id,
        symbol = token.symbol,
        name = token.name,
        image = token.image,
        id_ = token.id,
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
      )
    }
  }

  suspend fun insertSelectedReferenceToken(token: SelectedToken) {
    withContext(dispatcher) {
      dbQueries.insertReferenceToken(
        id = token.id,
        symbol = token.symbol,
        name = token.name,
        image = token.image,
        id_ = token.id,
        updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
      )
    }
  }

  fun selectMostRecentMainTokenFlow(): Flow<SelectedToken?> =
    dbQueries.selectMostRecentMainToken(::SelectedToken).asFlow().mapToOneOrNull(dispatcher)

  fun selectMostRecentMainTokenIdWithChartPeriodFlow(): Flow<Pair<String, MarketChartDaysPeriod>?> =
    dbQueries
      .selectMostRecentMainTokenIdWithChartPeriod { id, period -> id to period }
      .asFlow()
      .mapToOneOrNull(dispatcher)

  suspend fun selectMostRecentMainToken(): SelectedToken? =
    withContext(dispatcher) {
      dbQueries.selectMostRecentMainToken(::SelectedToken).executeAsOneOrNull()
    }

  fun selectMostRecentReferenceTokenFlow(): Flow<SelectedToken?> =
    dbQueries.selectMostRecentReferenceToken(::SelectedToken).asFlow().mapToOneOrNull(dispatcher)

  suspend fun selectMostRecentReferenceToken(): SelectedToken? =
    withContext(dispatcher) {
      dbQueries.selectMostRecentReferenceToken(::SelectedToken).executeAsOneOrNull()
    }

  fun selectChartPeriodFlow(): Flow<MarketChartDaysPeriod?> =
    dbQueries.selectChartPeriod().asFlow().mapToOneOrNull(dispatcher)
}
