package com.trm.coinvision.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
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
import app.cash.paging.compose.LazyPagingItems
import com.trm.coinvision.core.domain.model.CoinMarketsItem

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TokensSearchBar(
  modifier: Modifier = Modifier,
  tokensSearchBarState: TokensSearchBarState,
  onQueryChange: (String) -> Unit = {},
  tokensListState: LazyListState = rememberLazyListState(),
  // TODO: maybe pass CombinedLoadStates here instead (for easier previews)?
  tokens: LazyPagingItems<CoinMarketsItem>
) {
  DockedSearchBar(
    modifier = modifier,
    query = tokensSearchBarState.query,
    onQueryChange = {
      tokensSearchBarState.query = it
      onQueryChange(it)
    },
    onSearch = { tokensSearchBarState.active = false },
    active = tokensSearchBarState.active,
    onActiveChange = { tokensSearchBarState.active = it },
    placeholder = { Text("Hinted search text") },
    leadingIcon = {
      IconButton({ tokensSearchBarState.active = !tokensSearchBarState.active }) {
        if (tokensSearchBarState.active) {
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
          items(tokens.itemCount) { index ->
            tokens[index]?.let {
              ListItem(
                modifier =
                  Modifier.clickable {
                    tokensSearchBarState.query = it.name
                    onQueryChange(it.name)
                    tokensSearchBarState.active = false
                  },
                headlineContent = {
                  Text(text = it.name, style = MaterialTheme.typography.titleMedium)
                },
                supportingContent = {
                  Text(
                    text = it.currentPrice.toString(),
                    style = MaterialTheme.typography.titleMedium
                  )
                },
                leadingContent = { Icon(Icons.Rounded.Star, contentDescription = it.name) },
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
class TokensSearchBarState(
  query: String = "",
  active: Boolean = false,
) {
  var query by mutableStateOf(query)
  var active by mutableStateOf(active)

  companion object {
    // 4
    val Saver =
      Saver<TokensSearchBarState, List<Any>>(
        save = { listOf(it.query, it.active) },
        restore = { TokensSearchBarState(query = it[0] as String, active = it[1] as Boolean) }
      )
  }
}

@Composable
fun rememberTokensSearchBarState(
  vararg inputs: Any?,
  init: () -> TokensSearchBarState = { TokensSearchBarState() }
): TokensSearchBarState =
  rememberSaveable(inputs = inputs, saver = TokensSearchBarState.Saver, init = init)
