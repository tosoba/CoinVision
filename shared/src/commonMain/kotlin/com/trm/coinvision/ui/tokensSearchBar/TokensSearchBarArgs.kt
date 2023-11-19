package com.trm.coinvision.ui.tokensSearchBar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.trm.coinvision.core.domain.model.TokenListItemDTO

@Stable
internal data class TokensSearchBarArgs(
  val searchBarState: TokensSearchBarState,
  val tokensListState: LazyListState,
  val tokens: LazyPagingItems<TokenListItemDTO>,
  val onQueryChange: (String) -> Unit = {},
  val onActiveChange: (Boolean) -> Unit = {},
  val onTokenSelected: (TokenListItemDTO) -> Unit = {}
)

@Composable
internal fun rememberTokensSearchBarArgs(viewModel: TokensSearchBarViewModel): TokensSearchBarArgs {
  val initialTokenSearchBarState by viewModel.initialSearchBarStateFlow.collectAsState()
  val tokensSearchBarState =
    rememberTokensSearchBarState(initialTokenSearchBarState) { initialTokenSearchBarState }

  val tokensListState = rememberLazyListState()
  val tokens = viewModel.tokensPagingFlow.collectAsLazyPagingItems()

  return remember(viewModel, tokensSearchBarState, tokens, tokensListState) {
    TokensSearchBarArgs(
      searchBarState = tokensSearchBarState,
      tokensListState = tokensListState,
      tokens = tokens,
      onQueryChange = viewModel::onQueryChange,
      onActiveChange = { viewModel.onActiveChange() },
      onTokenSelected = viewModel::onTokenSelected
    )
  }
}
