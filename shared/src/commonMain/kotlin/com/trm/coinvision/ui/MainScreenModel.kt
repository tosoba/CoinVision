package com.trm.coinvision.ui

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.Ready
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.model.WithData
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import com.trm.coinvision.ui.common.TokensSearchBarState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class MainScreenModel(
  private val selectedTokenRepository: SelectedTokenRepository,
  private val tokenListRepository: TokenListPagingRepository
) : ScreenModel {
  private val queryFlow = MutableSharedFlow<String>()

  val searchBarTokensPagingFlow: StateFlow<PagingData<TokenListItemDTO>> =
    queryFlow
      .map { it.takeIf { it.length > 2 } }
      .distinctUntilChanged()
      .debounce(500L)
      .flatMapLatest { tokenListRepository(it).cachedIn(coroutineScope) }
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )

  val initialTokenSearchBarStateFlow: StateFlow<TokensSearchBarState> =
    flow {
        emit(LoadingFirst)
        emit(Ready(selectedTokenRepository.getSelectedMainToken()))
      }
      .map {
        when (it) {
          is WithData -> {
            val (id, symbol, name, image) = it.data
            TokensSearchBarState(
              query = name,
              selectedTokenId = id,
              selectedTokenSymbol = symbol,
              selectedTokenImage = image,
              isLoading = false
            )
          }
          else -> {
            TokensSearchBarState(isLoading = true)
          }
        }
      }
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

  private fun resetSearch() {
    coroutineScope.launch { queryFlow.emit("") }
  }

  fun onTokenSelected(token: TokenListItemDTO) {
    resetSearch()
    coroutineScope.launch {
      selectedTokenRepository.updateSelectedMainToken(
        SelectedToken(
          id = token.id,
          symbol = token.symbol,
          name = token.name,
          image = token.image,
        )
      )
    }
  }
}
