package com.trm.coinvision.ui.compareTokens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenWithChartFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedReferenceTokenFlowUseCase
import com.trm.coinvision.ui.chart.PriceChartPoint
import com.trm.coinvision.ui.chart.toPriceChartPoints
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class CompareTokensViewModel(
  getSelectedMainTokenWithChartFlowUseCase: GetSelectedMainTokenWithChartFlowUseCase,
  getSelectedReferenceTokenFlowUseCase: GetSelectedReferenceTokenFlowUseCase,
  private val swapSelectedTokens: suspend () -> Unit,
  private val updateChartPeriod: suspend (MarketChartDaysPeriod) -> Unit,
  getChartPeriodFlow: () -> Flow<MarketChartDaysPeriod>,
) : ViewModel() {
  private val retryMainTokenWithChartFlow = MutableSharedFlow<Unit>()

  val mainTokenWithChartFlow: StateFlow<Loadable<Pair<TokenDTO, List<PriceChartPoint>>>> =
    retryMainTokenWithChartFlow
      .onStart { emit(Unit) }
      .flatMapLatest {
        getSelectedMainTokenWithChartFlowUseCase().map {
          it.map { (token, marketChart) -> token to marketChart.toPriceChartPoints() }
        }
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadingFirst,
      )

  fun onRetryMainTokenWithChartClick() {
    viewModelScope.launch { retryMainTokenWithChartFlow.emit(Unit) }
  }

  private val retryReferenceTokenFlow = MutableSharedFlow<Unit>()

  val referenceTokenFlow: StateFlow<Loadable<TokenDTO>> =
    retryReferenceTokenFlow
      .onStart { emit(Unit) }
      .flatMapLatest { getSelectedReferenceTokenFlowUseCase() }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadingFirst,
      )

  fun onRetryReferenceTokenClick() {
    viewModelScope.launch { retryReferenceTokenFlow.emit(Unit) }
  }

  fun onSwapTokensClick() {
    viewModelScope.launch { swapSelectedTokens() }
  }

  val chartPeriod: StateFlow<MarketChartDaysPeriod> =
    getChartPeriodFlow()
      .stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = MarketChartDaysPeriod.default,
      )

  fun onChartPeriodClick(period: MarketChartDaysPeriod) {
    viewModelScope.launch { updateChartPeriod(period) }
  }
}
