package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.Loading
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.model.WithData
import com.trm.coinvision.core.domain.model.dataOrNull
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenWithChartFlowUseCase
import com.trm.coinvision.ui.chart.PriceChartPoint
import com.trm.coinvision.ui.chart.toPriceChartPoints
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class TokensListScreenModel(
  tokenListPagingRepository: TokenListPagingRepository,
  getSelectedMainTokenWithChartFlowUseCase: GetSelectedMainTokenWithChartFlowUseCase,
  private val updateChartPeriod: suspend (MarketChartDaysPeriod) -> Unit,
  getChartPeriodFlow: () -> Flow<MarketChartDaysPeriod>
) : ScreenModel {
  private val retryMainTokenWithChartFlow = MutableSharedFlow<Unit>()

  val selectedMainTokenWithChartFlow: StateFlow<Loadable<Pair<TokenDTO, List<PriceChartPoint>>>> =
    retryMainTokenWithChartFlow
      .onStart { emit(Unit) }
      .flatMapLatest {
        getSelectedMainTokenWithChartFlowUseCase().map {
          it.map { (token, marketChart) -> token to marketChart.toPriceChartPoints() }
        }
      }
      .stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  fun onRetryMainTokenWithChartClick() {
    screenModelScope.launch { retryMainTokenWithChartFlow.emit(Unit) }
  }

  val tokenPotentialComparisonPagingFlow: StateFlow<PagingData<TokenPotentialComparison>> =
    tokenListPagingRepository(null)
      .cachedIn(screenModelScope)
      .combine(
        selectedMainTokenWithChartFlow
          .map { it.map { (token) -> token } }
          .withIndex()
          .filter { (index, value) -> index == 0 || value !is Loading }
          .map { it.value }
          .distinctUntilChangedBy(Loadable<TokenDTO>::dataOrNull)
      ) { paging, token ->
        with(paging) {
          if (token is WithData) {
            map {
              TokenPotentialComparison(
                subjectToken = it,
                potential =
                  TokenPotential(
                    token = token.data,
                    potentialPrice = 0.0,
                    potentialUpsidePercentage = 0.0
                  )
              )
            }
          } else {
            map { TokenPotentialComparison(subjectToken = it, potential = null) }
          }
        }
      }
      .stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )

  val chartPeriod: StateFlow<MarketChartDaysPeriod> =
    getChartPeriodFlow()
      .stateIn(
        screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = MarketChartDaysPeriod.default
      )

  fun onChartPeriodClick(period: MarketChartDaysPeriod) {
    screenModelScope.launch { updateChartPeriod(period) }
  }
}
