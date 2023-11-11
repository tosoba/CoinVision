package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.TokenListItem
import com.trm.coinvision.core.domain.usecase.GetTokensPagingUseCase
import com.trm.coinvision.core.domain.usecase.SelectedTokenFlowUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class TokensListScreenModel(
  getTokensPagingUseCase: GetTokensPagingUseCase,
  selectedTokenFlowUseCase: SelectedTokenFlowUseCase
) : ScreenModel {
  val selectedToken: StateFlow<TokenListItem?> =
    selectedTokenFlowUseCase.stateIn(
      scope = coroutineScope,
      started = SharingStarted.WhileSubscribed(5_000L),
      initialValue = null
    )

  val tokensPagingFlow: StateFlow<PagingData<TokenListItem>> =
    getTokensPagingUseCase()
      .cachedIn(coroutineScope)
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )
}
