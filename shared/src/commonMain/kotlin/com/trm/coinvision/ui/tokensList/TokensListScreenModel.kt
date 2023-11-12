package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class TokensListScreenModel(
  tokenListPagingRepository: TokenListPagingRepository,
  selectedTokenRepository: SelectedTokenRepository
) : ScreenModel {
  val selectedToken =
    selectedTokenRepository
      .getSelectedTokenIdFlow()
      .transformLatest {
        emit(Loadable.InProgress)
        emit(Loadable.Completed(selectedTokenRepository.getTokenById(it)))
      }
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = Loadable.InProgress
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
