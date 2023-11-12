package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

internal class TokensListScreenModel(
  tokenListPagingRepository: TokenListPagingRepository,
  selectedTokenRepository: SelectedTokenRepository
) : ScreenModel {
  val selectedToken: SharedFlow<Result<TokenDTO>> =
    selectedTokenRepository
      .getSelectedTokenResultFlow()
      .shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
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
