package com.trm.coinvision.ui.tokensList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.ui.common.CoinVisionProgressIndicator
import com.trm.coinvision.ui.common.CoinVisionRetryColumn
import com.trm.coinvision.ui.common.CoinVisionRetryRow
import com.trm.coinvision.ui.common.SelectedTokenData
import com.trm.coinvision.ui.common.TokensSearchBar
import com.trm.coinvision.ui.common.TokensSearchBarState
import com.trm.coinvision.ui.common.tokensSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal class TokensListTab(
  private val searchBarState: TokensSearchBarState,
  private val tokensListState: LazyListState,
  private val tokens: LazyPagingItems<TokenListItemDTO>,
  private val onQueryChange: (String) -> Unit,
  private val onActiveChange: (Boolean) -> Unit,
  private val onTokenSelected: (TokenListItemDTO) -> Unit
) : Tab {
  @Composable
  override fun Content() {
    @Composable
    fun MainTokensSearchBar() {
      TokensSearchBar(
        modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
        searchBarState = searchBarState,
        tokensListState = tokensListState,
        tokens = tokens,
        onQueryChange = onQueryChange,
        onActiveChange = onActiveChange,
        onTokenSelected = onTokenSelected
      )
    }

    val screenModel = getScreenModel<TokensListScreenModel>()
    val token by screenModel.selectedToken.collectAsState()
    val listState = rememberLazyListState()

    if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
      Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          MainTokensSearchBar()
          SelectedTokenData(modifier = Modifier.fillMaxSize(), token = token)
        }
        CoinMarketsColumn(modifier = Modifier.weight(.5f).fillMaxHeight(), state = listState)
      }
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        MainTokensSearchBar()
        CoinMarketsColumn(modifier = Modifier.fillMaxSize(), state = listState)
      }
    }
  }

  @Composable
  private fun CoinMarketsColumn(modifier: Modifier = Modifier, state: LazyListState) {
    val screenModel = getScreenModel<TokensListScreenModel>()
    val tokens = screenModel.tokensPagingFlow.collectAsLazyPagingItems()
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(10.dp), state = state) {
      if (tokens.loadState.prepend is LoadState.Error) {
        item {
          CoinVisionRetryRow(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            onRetryClick = tokens::retry
          )
        }
      } else if (tokens.loadState.prepend == LoadState.Loading) {
        item { CoinVisionProgressIndicator(modifier = Modifier.padding(20.dp)) }
      }

      when (tokens.loadState.refresh) {
        is LoadState.Error -> {
          item {
            CoinVisionRetryColumn(
              modifier = Modifier.fillParentMaxSize(),
              onRetryClick = tokens::retry
            )
          }
        }
        LoadState.Loading -> {
          item { CoinVisionProgressIndicator(modifier = Modifier.fillParentMaxSize()) }
        }
        is LoadState.NotLoading -> {
          items(tokens.itemCount) { index -> tokens[index]?.let { Text(text = it.name) } }
        }
      }

      if (tokens.loadState.append is LoadState.Error) {
        item {
          CoinVisionRetryRow(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            onRetryClick = tokens::retry
          )
        }
      } else if (tokens.loadState.prepend == LoadState.Loading) {
        item { CoinVisionProgressIndicator(modifier = Modifier.padding(20.dp)) }
      }
    }
  }

  @OptIn(ExperimentalResourceApi::class)
  override val options: TabOptions
    @Composable
    get() =
      TabOptions(
        index = 1u,
        title = LocalStringResources.current.list,
        icon = painterResource("list.xml")
      )
}
