package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenWithChartFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedReferenceTokenFlowUseCase
import com.trm.coinvision.ui.chart.PriceChartPoint
import com.trm.coinvision.ui.chart.toPriceChartPoints
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

internal class CompareTokensScreenModel(
  getSelectedMainTokenWithChartFlowUseCase: GetSelectedMainTokenWithChartFlowUseCase,
  getSelectedReferenceTokenFlowUseCase: GetSelectedReferenceTokenFlowUseCase,
) : ScreenModel, KoinComponent {
  val mainTokenWithChartFlow: StateFlow<Loadable<Pair<TokenDTO, List<PriceChartPoint>>>> =
    getSelectedMainTokenWithChartFlowUseCase(MarketChartDaysPeriod.DAY)
      .map { it.map { (token, marketChart) -> token to marketChart.toPriceChartPoints() } }
      .stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  val referenceTokenFlow: StateFlow<Loadable<TokenDTO>> =
    getSelectedReferenceTokenFlowUseCase()
      .stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  val referenceTokensSearchBarViewModel: TokensSearchBarViewModel by
    inject(named(TokensSearchBarType.REFERENCE)) { parametersOf(screenModelScope) }
}
