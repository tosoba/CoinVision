package com.trm.coinvision.ui.tokensSearchBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.back
import coinvision.shared.generated.resources.loading
import coinvision.shared.generated.resources.search
import coinvision.shared.generated.resources.search_for_tokens
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.ui.common.CoinVisionProgressIndicator
import com.trm.coinvision.ui.common.CoinVisionRetryColumn
import com.trm.coinvision.ui.common.CoinVisionRetryRow
import com.trm.coinvision.ui.common.TokenImageOrSymbol
import com.trm.coinvision.ui.common.TokenSymbol
import com.trm.coinvision.ui.common.errorText
import com.trm.coinvision.ui.common.tokenSymbolShape
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TokensSearchBar(modifier: Modifier = Modifier, viewModel: TokensSearchBarViewModel) {
  val tokensListState = rememberSaveable(saver = LazyListState.Saver) { viewModel.tokensListState }
  val tokens = viewModel.tokensPagingFlow.collectAsLazyPagingItems()

  TokensSearchBar(
    modifier = modifier,
    query = viewModel.query,
    selectedToken = viewModel.selectedToken,
    active = viewModel.active,
    isLoading = viewModel.isLoading,
    tokensListState = tokensListState,
    tokens = tokens,
    onQueryChange = viewModel::onQueryChange,
    onActiveChange = viewModel::onActiveChange,
    onTokenSelected = viewModel::onTokenSelected,
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TokensSearchBar(
  modifier: Modifier = Modifier,
  query: String = "",
  selectedToken: SelectedToken,
  active: Boolean = false,
  isLoading: Boolean = false,
  tokensListState: LazyListState = rememberLazyListState(),
  tokens: LazyPagingItems<TokenListItemDTO> =
    flowOf(PagingData.empty<TokenListItemDTO>()).collectAsLazyPagingItems(),
  onQueryChange: (String) -> Unit = {},
  onActiveChange: (Boolean) -> Unit = {},
  onTokenSelected: (TokenListItemDTO) -> Unit = {},
) {
  Column(modifier = modifier) {
    DockedSearchBar(
      modifier = Modifier.fillMaxWidth(),
      enabled = !isLoading,
      query = query,
      onQueryChange = { onQueryChange(it) },
      onSearch = {},
      active = active,
      onActiveChange = { onActiveChange(it) },
      placeholder = {
        Text(stringResource(if (isLoading) Res.string.loading else Res.string.search_for_tokens))
      },
      leadingIcon = {
        IconButton({ onActiveChange(!active) }) {
          if (active) {
            Icon(
              Icons.AutoMirrored.Rounded.ArrowBack,
              contentDescription = stringResource(Res.string.back),
            )
          } else {
            Icon(Icons.Rounded.Search, contentDescription = stringResource(Res.string.search))
          }
        }
      },
      trailingIcon = {
        AnimatedVisibility(
          visible = selectedToken.image != null,
          enter = fadeIn(),
          exit = fadeOut(),
        ) {
          TokenImageOrSymbol(
            modifier = Modifier.size(40.dp).clip(CircleShape),
            image = selectedToken.image,
            symbol = selectedToken.symbol,
            name = selectedToken.name,
          )
        }
      },
    ) {
      LazyColumn(modifier = Modifier.fillMaxWidth(), state = tokensListState) {
        when (val prepend = tokens.loadState.prepend) {
          is LoadState.Error -> {
            item {
              CoinVisionRetryRow(
                modifier = Modifier.fillMaxWidth().padding(20.dp).animateItem(),
                text = prepend.error.errorText(),
                onRetryClick = tokens::retry,
              )
            }
          }
          LoadState.Loading -> {
            item { CoinVisionProgressIndicator(modifier = Modifier.padding(20.dp)) }
          }
          else -> {}
        }

        when (val refresh = tokens.loadState.refresh) {
          is LoadState.Error -> {
            item {
              CoinVisionRetryColumn(
                modifier = Modifier.fillParentMaxSize().animateItem(),
                text = refresh.error.errorText(),
                onRetryClick = tokens::retry,
              )
            }
          }
          LoadState.Loading -> {
            items(100) {
              ListItem(
                modifier = Modifier.animateItem(),
                headlineContent = { Box(modifier = Modifier.shimmerListItemContent()) },
                supportingContent = { Box(modifier = Modifier.shimmerListItemContent()) },
                leadingContent = { TokenSymbol(modifier = Modifier.shimmer().tokenSymbolShape()) },
              )
            }
          }
          is LoadState.NotLoading -> {
            items(count = tokens.itemCount, key = tokens.itemKey(TokenListItemDTO::id)) { index ->
              tokens[index]?.let { token ->
                ListItem(
                  modifier = Modifier.clickable { onTokenSelected(token) }.animateItem(),
                  headlineContent = {
                    Text(text = token.name, style = MaterialTheme.typography.titleMedium)
                  },
                  supportingContent = {
                    Text(
                      text = token.currentPrice.toString(),
                      style = MaterialTheme.typography.titleMedium,
                    )
                  },
                  leadingContent = {
                    TokenImageOrSymbol(
                      modifier = Modifier.size(40.dp).clip(CircleShape),
                      image = token.image,
                      symbol = token.symbol,
                      name = token.name,
                    )
                  },
                  trailingContent = {
                    AnimatedVisibility(visible = selectedToken.id == token.id) {
                      Icon(Icons.Default.Check, contentDescription = null)
                    }
                  },
                )
              }
            }
          }
        }

        when (val append = tokens.loadState.append) {
          is LoadState.Error -> {
            item {
              CoinVisionRetryRow(
                modifier = Modifier.fillMaxWidth().padding(20.dp).animateItem(),
                text = append.error.errorText(),
                onRetryClick = tokens::retry,
              )
            }
          }
          LoadState.Loading -> {
            item { CoinVisionProgressIndicator(modifier = Modifier.padding(20.dp).animateItem()) }
          }
          else -> {}
        }
      }
    }

    AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut()) {
      LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
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

internal val tabElementPadding = 10.dp
