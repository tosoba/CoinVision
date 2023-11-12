package com.trm.coinvision.ui

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class MainScreenModel(
  private val tokenListRepository: TokenListPagingRepository,
  private val selectedTokenRepository: SelectedTokenRepository
) : ScreenModel {
  private val queryFlow = MutableSharedFlow<String>()

  val tokensPagingFlow: StateFlow<PagingData<TokenListItemDTO>> =
    queryFlow
      .map { it.takeIf(String::isNotBlank) }
      .distinctUntilChanged()
      .debounce(500L)
      .flatMapLatest { tokenListRepository(it).cachedIn(coroutineScope) }
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
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
      selectedTokenRepository.saveSelectedToken(
        id = token.id,
        name = token.name,
        image = token.image
      )
    }
  }

  private fun resetSearch() {
    coroutineScope.launch { queryFlow.emit("") }
  }
}
