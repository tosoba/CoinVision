package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedReferenceTokenFlowUseCase
import com.trm.coinvision.core.domain.model.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

internal class CompareTokensScreenModel(
  getSelectedMainTokenFlowUseCase: GetSelectedMainTokenFlowUseCase,
  getSelectedReferenceTokenFlowUseCase: GetSelectedReferenceTokenFlowUseCase,
) : ScreenModel, KoinComponent {
  val selectedMainTokenFlow: StateFlow<Loadable<TokenDTO>> =
    getSelectedMainTokenFlowUseCase()
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  val selectedReferenceTokenFlow: StateFlow<Loadable<TokenDTO>> =
    getSelectedReferenceTokenFlowUseCase()
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  val referenceTokensSearchBarViewModel: TokensSearchBarViewModel by
    inject(named(TokensSearchBarType.REFERENCE)) { parametersOf(coroutineScope) }
}
