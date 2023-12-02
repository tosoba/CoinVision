package com.trm.coinvision.core.domain.repo

import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.model.MarketChartDTO
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.coroutines.flow.Flow

internal interface TokenRepository {
  suspend fun updateSelectedMainToken(token: SelectedToken)

  fun getSelectedMainTokenFlow(): Flow<SelectedToken>

  fun getSelectedMainTokenIdWithChartPeriodFlow(): Flow<Pair<String, MarketChartDaysPeriod>>

  suspend fun updateSelectedReferenceToken(token: SelectedToken)

  fun getSelectedReferenceTokenFlow(): Flow<SelectedToken>

  fun getSelectedReferenceTokenIdFlow(): Flow<String>

  suspend fun swapSelectedTokens()

  suspend fun getTokenById(id: String): TokenDTO

  suspend fun getTokenChart(
    id: String,
    vsFiatCurrency: FiatCurrency,
    days: MarketChartDaysPeriod
  ): MarketChartDTO
}
