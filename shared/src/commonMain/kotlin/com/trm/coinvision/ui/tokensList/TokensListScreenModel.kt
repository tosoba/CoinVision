package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.usecase.GetTokensPagingUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedTokenFlowUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class TokensListScreenModel(
  getTokensPagingUseCase: GetTokensPagingUseCase,
  getSelectedTokenFlowUseCase: GetSelectedTokenFlowUseCase
) : ScreenModel {
  val selectedToken: StateFlow<TokenListItemDTO?> =
    getSelectedTokenFlowUseCase.stateIn(
      scope = coroutineScope,
      started = SharingStarted.WhileSubscribed(5_000L),
      initialValue = null
    )

  val tokensPagingFlow: StateFlow<PagingData<TokenListItemDTO>> =
    getTokensPagingUseCase()
      .cachedIn(coroutineScope)
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )
}
