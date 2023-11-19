package com.trm.coinvision.ui.tokensSearchBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.model.WithData
import com.trm.coinvision.ui.common.CoinVisionProgressIndicator
import com.trm.coinvision.ui.common.CoinVisionRetryColumn
import com.trm.coinvision.ui.common.CoinVisionRetryRow
import com.trm.coinvision.ui.common.TokenSymbol
import com.trm.coinvision.ui.common.tokenSymbolShape
import com.valentinilk.shimmer.shimmer
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
internal fun TokensSearchBar(modifier: Modifier = Modifier, args: TokensSearchBarArgs) {
  TokensSearchBar(
    modifier = modifier,
    searchBarState = args.searchBarState,
    deactivateFlow = args.deactivateFlow,
    tokensListState = args.tokensListState,
    tokens = args.tokens,
    onQueryChange = args.onQueryChange,
    onActiveChange = args.onActiveChange,
    onTokenSelected = args.onTokenSelected
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
internal fun TokensSearchBar(
  modifier: Modifier = Modifier,
  searchBarState: TokensSearchBarState = rememberTokensSearchBarState(),
  deactivateFlow: Flow<Unit> = emptyFlow(),
  tokensListState: LazyListState = rememberLazyListState(),
  tokens: LazyPagingItems<TokenListItemDTO> =
    flowOf(PagingData.empty<TokenListItemDTO>()).collectAsLazyPagingItems(),
  onQueryChange: (String) -> Unit = {},
  onActiveChange: (Boolean) -> Unit = {},
  onTokenSelected: (TokenListItemDTO) -> Unit = {}
) {
  LaunchedEffect(searchBarState) {
    deactivateFlow.onEach { searchBarState.updateActive(false) }.launchIn(this)
  }

  Column(modifier = modifier) {
    DockedSearchBar(
      modifier = Modifier.fillMaxWidth(),
      enabled = !searchBarState.isLoading,
      query = searchBarState.query,
      onQueryChange = {
        searchBarState.updateQuery(it)
        onQueryChange(it)
      },
      onSearch = {},
      active = searchBarState.active,
      onActiveChange = {
        searchBarState.updateActive(it)
        onActiveChange(it)
      },
      placeholder = {
        Text(
          if (searchBarState.isLoading) LocalStringResources.current.loading
          else LocalStringResources.current.searchForTokens
        )
      },
      leadingIcon = {
        IconButton({ searchBarState.updateActive(!searchBarState.active) }) {
          if (searchBarState.active) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
          } else {
            Icon(Icons.Rounded.Search, contentDescription = null)
          }
        }
      },
      trailingIcon = {
        AnimatedVisibility(
          visible = searchBarState.selectedTokenImage != null,
          enter = fadeIn(),
          exit = fadeOut(),
        ) {
          TokenImageOrSymbol(
            modifier = Modifier.size(40.dp).clip(CircleShape),
            image = searchBarState.selectedTokenImage,
            symbol = searchBarState.selectedTokenSymbol,
            name = searchBarState.selectedTokenName
          )
        }
      },
    ) {
      LazyColumn(modifier = Modifier.fillMaxWidth(), state = tokensListState) {
        if (tokens.loadState.prepend is LoadState.Error) {
          item {
            CoinVisionRetryRow(
              modifier = Modifier.fillMaxWidth().padding(20.dp).animateItemPlacement(),
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
                modifier = Modifier.fillParentMaxSize().animateItemPlacement(),
                onRetryClick = tokens::retry
              )
            }
          }
          LoadState.Loading -> {
            items(100) {
              ListItem(
                modifier = Modifier.animateItemPlacement(),
                headlineContent = { Box(modifier = Modifier.shimmerListItemContent()) },
                supportingContent = { Box(modifier = Modifier.shimmerListItemContent()) },
                leadingContent = { TokenSymbol(modifier = Modifier.shimmer().tokenSymbolShape()) },
              )
            }
          }
          is LoadState.NotLoading -> {
            items(
              count = tokens.itemCount,
              key = tokens.itemKey(TokenListItemDTO::id),
            ) { index ->
              tokens[index]?.let { token ->
                ListItem(
                  modifier =
                    Modifier.clickable {
                        searchBarState.onTokenSelected(token)
                        onTokenSelected(token)
                      }
                      .animateItemPlacement(),
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
                    TokenImageOrSymbol(
                      modifier = Modifier.size(40.dp).clip(CircleShape),
                      image = token.image,
                      symbol = token.symbol,
                      name = token.name
                    )
                  },
                  trailingContent = {
                    AnimatedVisibility(visible = searchBarState.selectedTokenId == token.id) {
                      Icon(Icons.Default.Check, contentDescription = null)
                    }
                  }
                )
              }
            }
          }
        }

        if (tokens.loadState.append is LoadState.Error) {
          item {
            CoinVisionRetryRow(
              modifier = Modifier.fillMaxWidth().padding(20.dp).animateItemPlacement(),
              onRetryClick = tokens::retry
            )
          }
        } else if (tokens.loadState.prepend == LoadState.Loading) {
          item {
            CoinVisionProgressIndicator(modifier = Modifier.padding(20.dp).animateItemPlacement())
          }
        }
      }
    }

    AnimatedVisibility(
      visible = searchBarState.isLoading,
      enter = fadeIn(),
      exit = fadeOut(),
    ) {
      LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
  }
}

@Composable
private fun TokenImageOrSymbol(
  modifier: Modifier = Modifier,
  image: String?,
  symbol: String,
  name: String
) {
  image?.let {
    KamelImage(
      modifier = modifier,
      resource = asyncPainterResource(data = Url(it)),
      contentDescription = name,
      onFailure = { TokenSymbol(symbol = symbol) },
      onLoading = { TokenSymbol(symbol = symbol, modifier = Modifier.tokenSymbolShape().shimmer()) }
    )
  } ?: run { TokenSymbol(symbol = symbol) }
}

@Stable
internal class TokensSearchBarState(
  query: String = "",
  selectedTokenId: String = "",
  selectedTokenSymbol: String = "",
  selectedTokenImage: String? = null,
  active: Boolean = false,
  val isLoading: Boolean = false,
) {
  var selectedTokenName by mutableStateOf(query)
    private set

  var query by mutableStateOf(query)
    private set

  var selectedTokenId by mutableStateOf(selectedTokenId)
    private set

  var selectedTokenSymbol by mutableStateOf(selectedTokenSymbol)
    private set

  var selectedTokenImage by mutableStateOf(selectedTokenImage)
    private set

  var active by mutableStateOf(active)
    private set

  fun updateQuery(query: String) {
    this.query = query
  }

  fun updateActive(active: Boolean) {
    this.active = active
    if (!active) query = selectedTokenName
  }

  fun onTokenSelected(token: TokenListItemDTO) {
    query = token.name
    selectedTokenId = token.id
    selectedTokenSymbol = token.symbol
    selectedTokenImage = token.image
    selectedTokenName = token.name
    active = false
  }

  companion object {
    val Saver =
      Saver<TokensSearchBarState, List<Any>>(
        save = {
          listOf(
            it.query,
            it.selectedTokenId,
            it.selectedTokenSymbol,
            it.selectedTokenImage.orEmpty(),
            it.active,
            it.isLoading
          )
        },
        restore = {
          TokensSearchBarState(
            query = it[0] as String,
            selectedTokenId = it[1] as String,
            selectedTokenSymbol = it[2] as String,
            selectedTokenImage = (it[3] as String).takeIf(String::isNotBlank),
            active = it[4] as Boolean,
            isLoading = it[5] as Boolean
          )
        }
      )
  }
}

@Composable
internal fun rememberTokensSearchBarState(
  vararg inputs: Any?,
  init: () -> TokensSearchBarState = { TokensSearchBarState() }
): TokensSearchBarState =
  rememberSaveable(inputs = inputs, saver = TokensSearchBarState.Saver, init = init)

internal fun selectedTokenLoadableToTokensSearchBarState(
  loadable: Loadable<SelectedToken>,
): TokensSearchBarState =
  when (loadable) {
    is WithData -> {
      val (id, symbol, name, image) = loadable.data
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

@Composable
private fun Modifier.shimmerListItemContent() =
  then(
    Modifier.fillMaxWidth(.5f)
      .height(20.dp)
      .padding(vertical = 2.dp)
      .shimmer()
      .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(5.dp))
  )

internal val tokensSearchBarPadding = 10.dp
