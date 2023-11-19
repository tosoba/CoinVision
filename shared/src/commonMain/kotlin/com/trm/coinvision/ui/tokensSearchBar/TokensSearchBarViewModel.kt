package com.trm.coinvision.ui.tokensSearchBar

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.Ready
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.usecase.DeactivateTokensSearchBarFlowUseCase
import com.trm.coinvision.core.domain.usecase.TokensSearchBarActiveChangeFlowUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class TokensSearchBarViewModel(
  private val coroutineScope: CoroutineScope,
  private val getSelectedTokenUseCase: suspend () -> SelectedToken,
  private val updateSelectedTokenUseCase: suspend (SelectedToken) -> Unit,
  private val getTokenListPagingUseCase: (String?) -> Flow<PagingData<TokenListItemDTO>>,
  deactivateSearchBarFlowUseCase: DeactivateTokensSearchBarFlowUseCase,
  private val searchBarActiveChangeFlowUseCase: TokensSearchBarActiveChangeFlowUseCase
) {
  private val queryFlow = MutableSharedFlow<String>()

  val tokensPagingFlow: StateFlow<PagingData<TokenListItemDTO>> =
    queryFlow
      .map { it.takeIf { it.length > 2 } }
      .distinctUntilChanged()
      .debounce(500L)
      .flatMapLatest { getTokenListPagingUseCase(it).cachedIn(coroutineScope) }
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )

  val initialSearchBarStateFlow: StateFlow<TokensSearchBarState> =
    flow {
        emit(LoadingFirst)
        emit(Ready(getSelectedTokenUseCase()))
      }
      .map(::selectedTokenLoadableToTokensSearchBarState)
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TokensSearchBarState(isLoading = true)
      )

  val deactivateFlow: SharedFlow<Unit> =
    deactivateSearchBarFlowUseCase()
      .shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
      )

  fun onActiveChange(active: Boolean) {
    resetSearch()
    coroutineScope.launch { searchBarActiveChangeFlowUseCase(active) }
  }

  fun onQueryChange(query: String) {
    coroutineScope.launch { queryFlow.emit(query) }
  }

  fun onTokenSelected(token: TokenListItemDTO) {
    resetSearch()
    coroutineScope.launch {
      updateSelectedTokenUseCase(
        SelectedToken(
          id = token.id,
          symbol = token.symbol,
          name = token.name,
          image = token.image,
        )
      )
    }
  }

  private fun resetSearch() {
    coroutineScope.launch { queryFlow.emit("") }
  }
}
