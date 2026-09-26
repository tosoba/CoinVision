package com.trm.coinvision.ui.tokensList

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.if_label
import coinvision.shared.generated.resources.reached_market_cap_of
import com.trm.coinvision.core.common.util.ext.toMarketCapFormat
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.ui.chart.PriceChart
import com.trm.coinvision.ui.chart.PriceChartHeader
import com.trm.coinvision.ui.chart.PriceChartPoint
import com.trm.coinvision.ui.common.AutoSizeText
import com.trm.coinvision.ui.common.CoinVisionProgressIndicator
import com.trm.coinvision.ui.common.CoinVisionRetryColumn
import com.trm.coinvision.ui.common.CoinVisionRetryRow
import com.trm.coinvision.ui.common.LoadableView
import com.trm.coinvision.ui.common.TokenImageOrSymbol
import com.trm.coinvision.ui.common.errorText
import com.trm.coinvision.ui.common.usingHorizontalTabSplit
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewState
import com.trm.coinvision.ui.tokensSearchBar.tabElementPadding
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@Composable
internal fun TokensListRoute(
  modifier: Modifier = Modifier,
  viewModel: TokensListViewModel = koinViewModel(),
  mainTokensSearchBarViewModel: TokensSearchBarViewModel =
    koinViewModel(qualifier = named(TokensSearchBarType.MAIN)),
) {
  val mainToken by viewModel.mainTokenFlow.collectAsState()
  val tokenPotentialComparisonItems =
    viewModel.tokenPotentialComparisonPagingFlow.collectAsLazyPagingItems()
  val searchTokens = mainTokensSearchBarViewModel.tokensPagingFlow.collectAsLazyPagingItems()
  val listState = rememberLazyListState()

  if (usingHorizontalTabSplit) {
    val chartPoints by viewModel.mainTokenChartPointsFlow.collectAsState()
    val chartPeriod by viewModel.chartPeriod.collectAsState()

    TokensListHorizontalSplit(
      modifier = modifier,
      searchBarState = mainTokensSearchBarViewModel.viewState,
      searchListState = mainTokensSearchBarViewModel.tokensListState,
      searchTokens = searchTokens,
      onSearchQueryChange = mainTokensSearchBarViewModel::onQueryChange,
      onSearchActiveChange = mainTokensSearchBarViewModel::onActiveChange,
      onSearchTokenSelected = mainTokensSearchBarViewModel::onTokenSelected,
      mainToken = mainToken,
      chartPeriod = chartPeriod,
      onRetryMainTokenWithChartClick = viewModel::onRetryMainTokenWithChartClick,
      onChartPeriodClick = viewModel::onChartPeriodClick,
      chartPoints = chartPoints,
      tokenPotentialComparisonItems = tokenPotentialComparisonItems,
      listState = listState,
    )
  } else {
    TokensList(
      modifier = modifier,
      searchBarState = mainTokensSearchBarViewModel.viewState,
      searchListState = mainTokensSearchBarViewModel.tokensListState,
      searchTokens = searchTokens,
      onSearchQueryChange = mainTokensSearchBarViewModel::onQueryChange,
      onSearchActiveChange = mainTokensSearchBarViewModel::onActiveChange,
      onSearchTokenSelected = mainTokensSearchBarViewModel::onTokenSelected,
      mainToken = mainToken,
      onRetryMainTokenWithChartClick = viewModel::onRetryMainTokenWithChartClick,
      tokenPotentialComparisonItems = tokenPotentialComparisonItems,
      listState = listState,
    )
  }
}

@Composable
private fun TokensList(
  modifier: Modifier = Modifier,
  searchBarState: TokensSearchBarViewState,
  searchListState: LazyListState,
  searchTokens: LazyPagingItems<TokenListItemDTO>,
  onSearchQueryChange: (String) -> Unit,
  onSearchActiveChange: (Boolean) -> Unit,
  onSearchTokenSelected: (TokenListItemDTO) -> Unit,
  mainToken: Loadable<TokenDTO>,
  onRetryMainTokenWithChartClick: () -> Unit,
  tokenPotentialComparisonItems: LazyPagingItems<TokenPotentialComparison>,
  listState: LazyListState,
) {
  Column(modifier = modifier) {
    TokensSearchBar(
      state = searchBarState,
      tokensListState = searchListState,
      tokens = searchTokens,
      modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
      onQueryChange = onSearchQueryChange,
      onActiveChange = onSearchActiveChange,
      onTokenSelected = onSearchTokenSelected,
    )

    LoadableView(
      modifier = Modifier.fillMaxSize(),
      loadable = mainToken,
      onRetryClick = onRetryMainTokenWithChartClick,
    ) {
      TokenPotentialComparisonLazyColumn(
        comparisonItems = tokenPotentialComparisonItems,
        modifier = Modifier.fillMaxSize(),
        state = listState,
      )
    }
  }
}

@Composable
private fun TokensListHorizontalSplit(
  modifier: Modifier = Modifier,
  searchBarState: TokensSearchBarViewState,
  searchListState: LazyListState,
  searchTokens: LazyPagingItems<TokenListItemDTO>,
  onSearchQueryChange: (String) -> Unit,
  onSearchActiveChange: (Boolean) -> Unit,
  onSearchTokenSelected: (TokenListItemDTO) -> Unit,
  mainToken: Loadable<TokenDTO>,
  chartPeriod: MarketChartDaysPeriod,
  onRetryMainTokenWithChartClick: () -> Unit,
  onChartPeriodClick: (MarketChartDaysPeriod) -> Unit,
  chartPoints: Loadable<List<PriceChartPoint>>,
  tokenPotentialComparisonItems: LazyPagingItems<TokenPotentialComparison>,
  listState: LazyListState,
) {
  Row(modifier = modifier) {
    Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
      TokensSearchBar(
        state = searchBarState,
        tokensListState = searchListState,
        tokens = searchTokens,
        modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
        onQueryChange = onSearchQueryChange,
        onActiveChange = onSearchActiveChange,
        onTokenSelected = onSearchTokenSelected,
      )

      PriceChartHeader(
        marketData = mainToken.mapNullable(block = TokenDTO::marketData),
        chartPeriod = chartPeriod,
        modifier = Modifier.fillMaxWidth().padding(horizontal = tabElementPadding),
        onChartPeriodClick = onChartPeriodClick,
      )

      LoadableView(
        modifier = Modifier.fillMaxSize().padding(tabElementPadding),
        loadable = chartPoints,
        onRetryClick = onRetryMainTokenWithChartClick,
      ) {
        PriceChart(points = it, modifier = Modifier.fillMaxSize())
      }
    }

    LoadableView(
      modifier = Modifier.weight(.5f).fillMaxHeight(),
      loadable = mainToken,
      onRetryClick = onRetryMainTokenWithChartClick,
    ) {
      TokenPotentialComparisonLazyColumn(
        comparisonItems = tokenPotentialComparisonItems,
        modifier = Modifier.fillMaxSize(),
        state = listState,
      )
    }
  }
}

@Composable
private fun TokenPotentialComparisonLazyColumn(
  comparisonItems: LazyPagingItems<TokenPotentialComparison>,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(bottom = 8.dp, start = 8.dp, end = 8.dp),
    state = state,
  ) {
    when (val prepend = comparisonItems.loadState.prepend) {
      is LoadState.Error -> {
        item {
          CoinVisionRetryRow(
            modifier = Modifier.fillMaxWidth().padding(16.dp).animateItem(),
            text = prepend.error.errorText(),
            onRetryClick = comparisonItems::retry,
          )
        }
      }
      LoadState.Loading -> {
        item { CoinVisionProgressIndicator(modifier = Modifier.padding(16.dp).animateItem()) }
      }
      else -> {}
    }

    when (val refresh = comparisonItems.loadState.refresh) {
      is LoadState.Error -> {
        item {
          CoinVisionRetryColumn(
            modifier = Modifier.fillParentMaxSize().animateItem(),
            text = refresh.error.errorText(),
            onRetryClick = comparisonItems::retry,
          )
        }
      }
      LoadState.Loading -> {
        item { CoinVisionProgressIndicator(modifier = Modifier.fillParentMaxSize().animateItem()) }
      }
      is LoadState.NotLoading -> {
        comparisonItems
          .takeIf { it.itemCount > 0 }
          ?.get(0)
          ?.potential
          ?.token
          ?.symbol
          ?.let { symbol ->
            item {
              TokenPotentialComparisonHeader(
                tokenSymbol = symbol,
                modifier =
                  Modifier.fillMaxWidth()
                    .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                    .animateItem(),
              )
            }
          }

        items(
          count = comparisonItems.itemCount,
          key = comparisonItems.itemKey { it.referenceToken.id },
        ) { index ->
          comparisonItems[index]?.let {
            TokenPotentialComparisonItem(
              item = it,
              index = index,
              modifier =
                Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp).animateItem(),
            )
          }
        }
      }
    }

    when (val append = comparisonItems.loadState.append) {
      is LoadState.Error -> {
        item {
          CoinVisionRetryRow(
            modifier = Modifier.fillMaxWidth().padding(16.dp).animateItem(),
            text = append.error.errorText(),
            onRetryClick = comparisonItems::retry,
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

@Composable
private fun TokenPotentialComparisonHeader(tokenSymbol: String, modifier: Modifier = Modifier) {
  AutoSizeText(
    modifier = modifier,
    text =
      buildAnnotatedString {
        append(stringResource(Res.string.if_label))
        append(' ')
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
          append(tokenSymbol.uppercase())
        }
        append(' ')
        append(stringResource(Res.string.reached_market_cap_of))
        append('…')
      },
    maxLines = 1,
    maxTextSize = MaterialTheme.typography.titleLarge.fontSize,
  )
}

@Composable
private fun TokenPotentialComparisonItem(
  item: TokenPotentialComparison,
  index: Int,
  modifier: Modifier = Modifier,
) {
  val (subjectToken, potential) = item

  ElevatedCard(modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = "${index + 1}",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
        maxLines = 1,
      )

      Spacer(modifier = Modifier.width(8.dp))

      TokenImageOrSymbol(
        image = subjectToken.image,
        symbol = subjectToken.symbol,
        name = subjectToken.name,
        modifier = Modifier.size(40.dp).clip(CircleShape),
      )

      Spacer(modifier = Modifier.width(8.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          modifier = Modifier.basicMarquee(),
          text = subjectToken.symbol.uppercase(),
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
          maxLines = 1,
        )
        Text(modifier = Modifier.basicMarquee(), text = subjectToken.name, maxLines = 1)
      }

      Spacer(modifier = Modifier.width(8.dp))

      Text(
        modifier = Modifier.weight(1f).basicMarquee(),
        text = subjectToken.marketCap?.toMarketCapFormat().orEmpty(),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        maxLines = 1,
      )

      potential?.let { (_, potentialPriceFormatted, potentialUpsideFormatted) ->
        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
          Text(
            modifier = Modifier.basicMarquee(),
            text = "$potentialPriceFormatted$",
            fontWeight = FontWeight.Medium,
            maxLines = 1,
          )
          Box(
            modifier =
              Modifier.clip(RoundedCornerShape(4.dp))
                .background(
                  color =
                    when {
                      potentialUpsideFormatted?.startsWith("+") == true ||
                        potentialUpsideFormatted?.endsWith("x") == true -> {
                        Color.Green
                      }
                      potentialUpsideFormatted?.startsWith("-") == true -> {
                        Color.Red
                      }
                      else -> {
                        Color.Transparent
                      }
                    }
                )
          ) {
            Text(
              modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp).basicMarquee(),
              text = potentialUpsideFormatted ?: "N/A",
              fontWeight = FontWeight.Medium,
              color =
                when {
                  potentialUpsideFormatted?.startsWith("+") == true ||
                    potentialUpsideFormatted?.endsWith("x") == true -> {
                    Color.Black
                  }
                  potentialUpsideFormatted?.startsWith("-") == true -> {
                    Color.White
                  }
                  else -> {
                    MaterialTheme.colorScheme.onPrimaryContainer
                  }
                },
              maxLines = 1,
            )
          }
        }
      }
    }
  }
}
