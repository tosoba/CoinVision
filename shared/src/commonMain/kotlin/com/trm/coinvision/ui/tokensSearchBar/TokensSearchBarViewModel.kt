package com.trm.coinvision.ui.tokensSearchBar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class TokensSearchBarViewModel(
  private val coroutineScope: CoroutineScope,
  getSelectedTokenFlow: () -> Flow<SelectedToken>,
  private val updateSelectedToken: suspend (SelectedToken) -> Unit,
  private val getTokenListPaging: (String?) -> Flow<PagingData<TokenListItemDTO>>
) {
  private val queryFlow = MutableSharedFlow<String>()

  var query by mutableStateOf("")
    private set

  var selectedToken by mutableStateOf(SelectedToken(id = "", symbol = "", name = "", image = null))
    private set

  var active by mutableStateOf(false)
    private set

  var isLoading by mutableStateOf(true)
    private set

  init {
    getSelectedTokenFlow()
      .onEach {
        query = it.name
        selectedToken = it
        isLoading = false
      }
      .launchIn(coroutineScope)
  }

  val tokensListState = LazyListState(0, 0)

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

  fun onActiveChange(active: Boolean) {
    this.active = active
    if (!active) query = selectedToken.name

    resetSearch()
  }

  fun onQueryChange(query: String) {
    this.query = query

    coroutineScope.launch { queryFlow.emit(query) }
  }

  fun onTokenSelected(token: TokenListItemDTO) {
    query = token.name
    selectedToken = SelectedToken(token.id, token.symbol, token.name, token.image)
    active = false

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
