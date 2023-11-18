package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenFlowUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class TokensListScreenModel(
  tokenListPagingRepository: TokenListPagingRepository,
  getSelectedMainTokenFlowUseCase: GetSelectedMainTokenFlowUseCase,
) : ScreenModel {
  val selectedToken: StateFlow<Loadable<TokenDTO>> =
    getSelectedMainTokenFlowUseCase()
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadingFirst
      )

  val tokensPagingFlow: StateFlow<PagingData<TokenListItemDTO>> =
    tokenListPagingRepository(null)
      .cachedIn(coroutineScope)
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )
}
