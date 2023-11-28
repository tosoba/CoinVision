package com.trm.coinvision.core.domain.usecase

import com.trm.coinvision.core.domain.model.FailedFirst
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.MarketChartDTO
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.Ready
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.repo.TokenRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

internal class GetSelectedMainTokenWithChartFlowUseCase(private val repository: TokenRepository) {
  @OptIn(ExperimentalCoroutinesApi::class)
  operator fun invoke(
    daysPeriod: MarketChartDaysPeriod
  ): Flow<Loadable<Pair<TokenDTO, MarketChartDTO>>> =
    repository.getSelectedMainTokenIdFlow().transformLatest {
      emit(LoadingFirst)
      coroutineScope {
        try {
          val token = async { repository.getTokenById(it) }
          val chart = async {
            repository.getTokenChart(id = it, vsFiatCurrency = FiatCurrency.USD, days = daysPeriod)
          }
          emit(Ready(token.await() to chart.await()))
        } catch (ex: CancellationException) {
          throw ex
        } catch (ex: Exception) {
          emit(FailedFirst(ex))
        }
      }
    }
}
