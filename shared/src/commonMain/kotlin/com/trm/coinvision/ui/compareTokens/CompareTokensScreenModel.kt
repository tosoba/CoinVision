package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.TokenListItem
import com.trm.coinvision.core.domain.usecase.SelectedTokenFlowUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class CompareTokensScreenModel(
  selectedTokenFlowUseCase: SelectedTokenFlowUseCase,
) : ScreenModel {
  val selectedToken: StateFlow<TokenListItem?> =
    selectedTokenFlowUseCase.stateIn(
      scope = coroutineScope,
      started = SharingStarted.WhileSubscribed(5_000L),
      initialValue = null
    )
}
