package com.trm.coinvision.ui.tokensSearchBar

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.Ready
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class TokensSearchBarViewModel(
  private val coroutineScope: CoroutineScope,
  private val getSelectedTokenFlow: suspend () -> Flow<SelectedToken>,
  private val updateSelectedToken: suspend (SelectedToken) -> Unit,
  private val getTokenListPaging: (String?) -> Flow<PagingData<TokenListItemDTO>>
) {
  private val queryFlow = MutableSharedFlow<String>()

  val tokensPagingFlow: StateFlow<PagingData<TokenListItemDTO>> =
    queryFlow
      .map { it.takeIf { it.length > 2 } }
      .distinctUntilChanged()
      .debounce(500L)
      .flatMapLatest { getTokenListPaging(it).cachedIn(coroutineScope) }
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )

  val searchBarStateFlow: StateFlow<TokensSearchBarState> =
    flow {
        emit(LoadingFirst)
        emitAll(getSelectedTokenFlow().map(::Ready))
      }
      .map(::selectedTokenLoadableToTokensSearchBarState)
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TokensSearchBarState(isLoading = true)
      )

  fun onActiveChange() {
    resetSearch()
  }

  fun onQueryChange(query: String) {
    coroutineScope.launch { queryFlow.emit(query) }
  }

  fun onTokenSelected(token: TokenListItemDTO) {
    resetSearch()
    coroutineScope.launch {
      updateSelectedToken(
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
