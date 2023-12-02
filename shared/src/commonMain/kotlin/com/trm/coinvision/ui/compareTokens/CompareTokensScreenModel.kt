package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenWithChartFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedReferenceTokenFlowUseCase
import com.trm.coinvision.ui.chart.PriceChartPoint
import com.trm.coinvision.ui.chart.toPriceChartPoints
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

@OptIn(ExperimentalCoroutinesApi::class)
internal class CompareTokensScreenModel(
  getSelectedMainTokenWithChartFlowUseCase: GetSelectedMainTokenWithChartFlowUseCase,
  getSelectedReferenceTokenFlowUseCase: GetSelectedReferenceTokenFlowUseCase,
  private val swapSelectedTokens: suspend () -> Unit
) : ScreenModel, KoinComponent {
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
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  fun onRetryMainTokenWithChartClick() {
    screenModelScope.launch { retryMainTokenWithChartFlow.emit(Unit) }
  }

  private val retryReferenceTokenFlow = MutableSharedFlow<Unit>()

  val referenceTokenFlow: StateFlow<Loadable<TokenDTO>> =
    retryReferenceTokenFlow
      .onStart { emit(Unit) }
      .flatMapLatest { getSelectedReferenceTokenFlowUseCase() }
      .stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  fun onRetryReferenceTokenClick() {
    screenModelScope.launch { retryReferenceTokenFlow.emit(Unit) }
  }

  val referenceTokensSearchBarViewModel: TokensSearchBarViewModel by
    inject(named(TokensSearchBarType.REFERENCE)) { parametersOf(screenModelScope) }

  fun onSwapTokensClick() {
    screenModelScope.launch { swapSelectedTokens() }
  }
}
