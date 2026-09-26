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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.back
import coinvision.shared.generated.resources.loading
import coinvision.shared.generated.resources.search
import coinvision.shared.generated.resources.search_for_tokens
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.ui.common.CoinVisionProgressIndicator
import com.trm.coinvision.ui.common.CoinVisionRetryColumn
import com.trm.coinvision.ui.common.CoinVisionRetryRow
import com.trm.coinvision.ui.common.TokenImageOrSymbol
import com.trm.coinvision.ui.common.TokenSymbol
import com.trm.coinvision.ui.common.errorText
import com.trm.coinvision.ui.common.tokenSymbolShape
import com.valentinilk.shimmer.shimmer
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TokensSearchBar(
  state: TokensSearchBarViewState,
  tokensListState: LazyListState,
  tokens: LazyPagingItems<TokenListItemDTO>,
  modifier: Modifier = Modifier,
  onQueryChange: (String) -> Unit,
  onActiveChange: (Boolean) -> Unit,
  onTokenSelected: (TokenListItemDTO) -> Unit,
) {
  Column(modifier = modifier) {
    DockedSearchBar(
      modifier = Modifier.fillMaxWidth(),
      enabled = !state.isLoading,
      query = state.query,
      onQueryChange = onQueryChange,
      onSearch = {},
      active = state.active,
      onActiveChange = onActiveChange,
      placeholder = {
        Text(
          text =
            stringResource(
              if (state.isLoading) Res.string.loading else Res.string.search_for_tokens
            )
        )
      },
      leadingIcon = {
        IconButton(onClick = { onActiveChange(!state.active) }) {
          if (state.active) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
              contentDescription = stringResource(Res.string.back),
            )
          } else {
            Icon(
              imageVector = Icons.Rounded.Search,
              contentDescription = stringResource(Res.string.search),
            )
          }
        }
      },
      trailingIcon = {
        AnimatedVisibility(
          visible = state.selectedToken.image != null,
          enter = fadeIn(),
          exit = fadeOut(),
        ) {
          TokenImageOrSymbol(
            image = state.selectedToken.image,
            symbol = state.selectedToken.symbol,
            name = state.selectedToken.name,
            modifier = Modifier.size(40.dp).clip(CircleShape),
          )
        }
      },
    ) {
      LazyColumn(modifier = Modifier.fillMaxWidth(), state = tokensListState) {
        when (val prepend = tokens.loadState.prepend) {
          is LoadState.Error -> {
            item {
              CoinVisionRetryRow(
                modifier = Modifier.fillMaxWidth().padding(16.dp).animateItem(),
                text = prepend.error.errorText(),
                onRetryClick = tokens::retry,
              )
            }
          }
          LoadState.Loading -> {
            item { CoinVisionProgressIndicator(modifier = Modifier.padding(16.dp).animateItem()) }
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
                leadingContent = {
                  TokenSymbol(symbol = "", modifier = Modifier.shimmer().tokenSymbolShape())
                },
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
                      image = token.image,
                      symbol = token.symbol,
                      name = token.name,
                      modifier = Modifier.size(40.dp).clip(CircleShape),
                    )
                  },
                  trailingContent = {
                    AnimatedVisibility(visible = state.selectedToken.id == token.id) {
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
                modifier = Modifier.fillMaxWidth().padding(16.dp).animateItem(),
                text = append.error.errorText(),
                onRetryClick = tokens::retry,
              )
            }
          }
          LoadState.Loading -> {
            item { CoinVisionProgressIndicator(modifier = Modifier.padding(16.dp).animateItem()) }
          }
          else -> {}
        }
      }
    }

    AnimatedVisibility(visible = state.isLoading, enter = fadeIn(), exit = fadeOut()) {
      LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
  }
}

@Composable
private fun Modifier.shimmerListItemContent() =
  then(
    Modifier.fillMaxWidth(.5f)
      .height(16.dp)
      .padding(vertical = 2.dp)
      .shimmer()
      .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
  )

internal val tabElementPadding = 16.dp
