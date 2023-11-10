package com.trm.coinvision.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.valentinilk.shimmer.shimmer
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.flow.flowOf

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TokensSearchBar(
  modifier: Modifier = Modifier,
  searchBarState: TokensSearchBarState = rememberTokensSearchBarState(),
  tokensListState: LazyListState = rememberLazyListState(),
  tokens: LazyPagingItems<CoinMarketsItem> =
    flowOf(PagingData.empty<CoinMarketsItem>()).collectAsLazyPagingItems()
) {
  DockedSearchBar(
    modifier = modifier,
    query = searchBarState.query,
    onQueryChange = searchBarState::updateQuery,
    onSearch = {},
    active = searchBarState.active,
    onActiveChange = searchBarState::updateActive,
    placeholder = { Text("Hinted search text") },
    leadingIcon = {
      IconButton({ searchBarState.updateActive(!searchBarState.active) }) {
        if (searchBarState.active) {
          Icon(Icons.Rounded.ArrowBack, contentDescription = null)
        } else {
          Icon(Icons.Rounded.Search, contentDescription = null)
        }
      }
    },
    trailingIcon = { Icon(Icons.Rounded.MoreVert, contentDescription = null) },
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxWidth(),
      state = tokensListState,
      contentPadding = PaddingValues(16.dp),
    ) {
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
          items(tokens.itemCount, key = { tokens[it]?.id.orEmpty() }) { index ->
            tokens[index]?.let { token ->
              ListItem(
                modifier = Modifier.clickable { searchBarState.selectToken(token) },
                headlineContent = {
                  Text(text = token.name, style = MaterialTheme.typography.titleMedium)
                },
                supportingContent = {
                  Text(
                    text = token.currentPrice.toString(),
                    style = MaterialTheme.typography.titleMedium
                  )
                },
                leadingContent = {
                  if (token.image != null) {
                    KamelImage(
                      modifier = Modifier.size(40.dp),
                      resource = asyncPainterResource(data = Url(token.image)),
                      contentDescription = token.name,
                      onFailure = { TokenSymbol(symbol = token.symbol) },
                      onLoading = {
                        TokenSymbol(
                          symbol = token.symbol,
                          modifier = Modifier.shimmer().tokenSymbolShape()
                        )
                      }
                    )
                  } else {
                    TokenSymbol(symbol = token.symbol)
                  }
                },
              )
            }
          }
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
}

@Stable
internal class TokensSearchBarState(
  query: String = "Bitcoin", // TODO: remove hardcoded shit lol
  active: Boolean = false,
  private val onQueryChange: (String) -> Unit = {},
  private val onActiveChange: (Boolean) -> Unit = {},
  private val onTokenSelected: (CoinMarketsItem) -> Unit = {}
) {
  private var previousSelectionName = query

  var query by mutableStateOf(query)
    private set

  var active by mutableStateOf(active)
    private set

  fun updateQuery(query: String) {
    this.query = query
    onQueryChange(query)
  }

  fun updateActive(active: Boolean) {
    this.active = active
    if (!active) query = previousSelectionName
    onActiveChange(active)
  }

  fun selectToken(token: CoinMarketsItem) {
    query = token.name
    previousSelectionName = token.name
    active = false
    onTokenSelected(token)
  }

  companion object {
    val Saver =
      Saver<TokensSearchBarState, List<Any>>(
        save = { listOf(it.query, it.active) },
        restore = { TokensSearchBarState(query = it[0] as String, active = it[1] as Boolean) }
      )
  }
}

@Composable
internal fun rememberTokensSearchBarState(
  vararg inputs: Any?,
  init: () -> TokensSearchBarState = { TokensSearchBarState() }
): TokensSearchBarState =
  rememberSaveable(inputs = inputs, saver = TokensSearchBarState.Saver, init = init)
