package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.swap_horizontal
import coinvision.shared.generated.resources.swap_tokens
import coinvision.shared.generated.resources.swap_vertical
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.ui.chart.PriceChart
import com.trm.coinvision.ui.chart.PriceChartHeader
import com.trm.coinvision.ui.chart.PriceChartPoint
import com.trm.coinvision.ui.common.LoadableView
import com.trm.coinvision.ui.common.TokenComparison
import com.trm.coinvision.ui.common.usingHorizontalTabSplit
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewState
import com.trm.coinvision.ui.tokensSearchBar.tabElementPadding
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@Composable
internal fun CompareTokensRoute(
  viewModel: CompareTokensViewModel = koinViewModel(),
  mainTokensSearchBarViewModel: TokensSearchBarViewModel =
    koinViewModel(qualifier = named(TokensSearchBarType.MAIN)),
  referenceTokensSearchBarViewModel: TokensSearchBarViewModel =
    koinViewModel(qualifier = named(TokensSearchBarType.REFERENCE)),
) {
  val mainTokenWithChart by viewModel.mainTokenWithChartFlow.collectAsState()
  val selectedReferenceToken by viewModel.referenceTokenFlow.collectAsState()
  val mainSearchTokens = mainTokensSearchBarViewModel.tokensPagingFlow.collectAsLazyPagingItems()
  val referenceSearchTokens =
    referenceTokensSearchBarViewModel.tokensPagingFlow.collectAsLazyPagingItems()
  val chartPeriod by viewModel.chartPeriod.collectAsState()
  val chartPeriodButtonScrollState = rememberScrollState()

  Box(modifier = Modifier.fillMaxSize()) {
    if (usingHorizontalTabSplit) {
      CompareTokensHorizontalSplit(
        mainTokenWithChart = mainTokenWithChart,
        referenceToken = selectedReferenceToken,
        chartPeriod = chartPeriod,
        chartPeriodButtonScrollState = chartPeriodButtonScrollState,
        mainSearchBarState = mainTokensSearchBarViewModel.viewState,
        mainSearchListState = mainTokensSearchBarViewModel.tokensListState,
        mainSearchTokens = mainSearchTokens,
        referenceSearchBarState = referenceTokensSearchBarViewModel.viewState,
        referenceSearchListState = referenceTokensSearchBarViewModel.tokensListState,
        referenceSearchTokens = referenceSearchTokens,
        onMainQueryChange = mainTokensSearchBarViewModel::onQueryChange,
        onMainActiveChange = mainTokensSearchBarViewModel::onActiveChange,
        onMainTokenSelected = mainTokensSearchBarViewModel::onTokenSelected,
        onReferenceQueryChange = referenceTokensSearchBarViewModel::onQueryChange,
        onReferenceActiveChange = referenceTokensSearchBarViewModel::onActiveChange,
        onReferenceTokenSelected = referenceTokensSearchBarViewModel::onTokenSelected,
        onChartPeriodClick = viewModel::onChartPeriodClick,
        onRetryMainTokenWithChartClick = viewModel::onRetryMainTokenWithChartClick,
        onRetryReferenceTokenClick = viewModel::onRetryReferenceTokenClick,
      )
    } else {
      CompareTokens(
        mainTokenWithChart = mainTokenWithChart,
        referenceToken = selectedReferenceToken,
        chartPeriod = chartPeriod,
        chartPeriodButtonScrollState = chartPeriodButtonScrollState,
        mainSearchBarState = mainTokensSearchBarViewModel.viewState,
        mainSearchListState = mainTokensSearchBarViewModel.tokensListState,
        mainSearchTokens = mainSearchTokens,
        referenceSearchBarState = referenceTokensSearchBarViewModel.viewState,
        referenceSearchListState = referenceTokensSearchBarViewModel.tokensListState,
        referenceSearchTokens = referenceSearchTokens,
        onMainQueryChange = mainTokensSearchBarViewModel::onQueryChange,
        onMainActiveChange = mainTokensSearchBarViewModel::onActiveChange,
        onMainTokenSelected = mainTokensSearchBarViewModel::onTokenSelected,
        onReferenceQueryChange = referenceTokensSearchBarViewModel::onQueryChange,
        onReferenceActiveChange = referenceTokensSearchBarViewModel::onActiveChange,
        onReferenceTokenSelected = referenceTokensSearchBarViewModel::onTokenSelected,
        onChartPeriodClick = viewModel::onChartPeriodClick,
        onRetryMainTokenWithChartClick = viewModel::onRetryMainTokenWithChartClick,
        onRetryReferenceTokenClick = viewModel::onRetryReferenceTokenClick,
      )
    }

    FloatingActionButton(
      modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
      onClick = viewModel::onSwapTokensClick,
    ) {
      Icon(
        painter =
          painterResource(
            if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
              Res.drawable.swap_horizontal
            } else {
              Res.drawable.swap_vertical
            }
          ),
        contentDescription = stringResource(Res.string.swap_tokens),
      )
    }
  }
}

@Composable
private fun CompareTokens(
  mainTokenWithChart: Loadable<Pair<TokenDTO, List<PriceChartPoint>>>,
  referenceToken: Loadable<TokenDTO>,
  chartPeriod: MarketChartDaysPeriod,
  chartPeriodButtonScrollState: ScrollState,
  mainSearchBarState: TokensSearchBarViewState,
  mainSearchListState: LazyListState,
  mainSearchTokens: LazyPagingItems<TokenListItemDTO>,
  referenceSearchBarState: TokensSearchBarViewState,
  referenceSearchListState: LazyListState,
  referenceSearchTokens: LazyPagingItems<TokenListItemDTO>,
  onMainQueryChange: (String) -> Unit,
  onMainActiveChange: (Boolean) -> Unit,
  onMainTokenSelected: (TokenListItemDTO) -> Unit,
  onReferenceQueryChange: (String) -> Unit,
  onReferenceActiveChange: (Boolean) -> Unit,
  onReferenceTokenSelected: (TokenListItemDTO) -> Unit,
  onChartPeriodClick: (MarketChartDaysPeriod) -> Unit,
  onRetryMainTokenWithChartClick: () -> Unit,
  onRetryReferenceTokenClick: () -> Unit,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    TokensSearchBar(
      state = mainSearchBarState,
      tokensListState = mainSearchListState,
      tokens = mainSearchTokens,
      modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
      onQueryChange = onMainQueryChange,
      onActiveChange = onMainActiveChange,
      onTokenSelected = onMainTokenSelected,
    )

    PriceChartHeader(
      marketData = mainTokenWithChart.mapNullable { (token) -> token.marketData },
      chartPeriod = chartPeriod,
      modifier =
        Modifier.fillMaxWidth()
          .horizontalScroll(chartPeriodButtonScrollState)
          .padding(horizontal = tabElementPadding),
      daysPeriodScrollState = chartPeriodButtonScrollState,
      onChartPeriodClick = onChartPeriodClick,
    )

    LoadableView(
      modifier = Modifier.fillMaxWidth().weight(.5f).padding(tabElementPadding),
      loadable = mainTokenWithChart.map { (_, chart) -> chart },
      onRetryClick = onRetryMainTokenWithChartClick,
    ) {
      PriceChart(points = it, modifier = Modifier.fillMaxSize())
    }

    TokensSearchBar(
      state = referenceSearchBarState,
      tokensListState = referenceSearchListState,
      tokens = referenceSearchTokens,
      modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
      onQueryChange = onReferenceQueryChange,
      onActiveChange = onReferenceActiveChange,
      onTokenSelected = onReferenceTokenSelected,
    )

    LoadableView(
      modifier = Modifier.fillMaxWidth().weight(.5f).padding(tabElementPadding),
      loadable1 = mainTokenWithChart.map { (token) -> token },
      loadable2 = referenceToken,
      onRetryClick1 = onRetryMainTokenWithChartClick,
      onRetryClick2 = onRetryReferenceTokenClick,
    ) { mainToken, selectedReferenceToken ->
      TokenComparison(
        mainToken = mainToken,
        referenceToken = selectedReferenceToken,
        modifier = Modifier.fillMaxSize(),
      )
    }
  }
}

@Composable
private fun CompareTokensHorizontalSplit(
  mainTokenWithChart: Loadable<Pair<TokenDTO, List<PriceChartPoint>>>,
  referenceToken: Loadable<TokenDTO>,
  chartPeriod: MarketChartDaysPeriod,
  chartPeriodButtonScrollState: ScrollState,
  mainSearchBarState: TokensSearchBarViewState,
  mainSearchListState: LazyListState,
  mainSearchTokens: LazyPagingItems<TokenListItemDTO>,
  referenceSearchBarState: TokensSearchBarViewState,
  referenceSearchListState: LazyListState,
  referenceSearchTokens: LazyPagingItems<TokenListItemDTO>,
  onMainQueryChange: (String) -> Unit,
  onMainActiveChange: (Boolean) -> Unit,
  onMainTokenSelected: (TokenListItemDTO) -> Unit,
  onReferenceQueryChange: (String) -> Unit,
  onReferenceActiveChange: (Boolean) -> Unit,
  onReferenceTokenSelected: (TokenListItemDTO) -> Unit,
  onChartPeriodClick: (MarketChartDaysPeriod) -> Unit,
  onRetryMainTokenWithChartClick: () -> Unit,
  onRetryReferenceTokenClick: () -> Unit,
) {
  Row(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
      TokensSearchBar(
        state = mainSearchBarState,
        tokensListState = mainSearchListState,
        tokens = mainSearchTokens,
        modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
        onQueryChange = onMainQueryChange,
        onActiveChange = onMainActiveChange,
        onTokenSelected = onMainTokenSelected,
      )

      PriceChartHeader(
        marketData = mainTokenWithChart.mapNullable { (token) -> token.marketData },
        chartPeriod = chartPeriod,
        modifier = Modifier.fillMaxWidth().padding(horizontal = tabElementPadding),
        daysPeriodScrollState = chartPeriodButtonScrollState,
        onChartPeriodClick = onChartPeriodClick,
      )

      LoadableView(
        modifier = Modifier.fillMaxSize(),
        loadable = mainTokenWithChart.map { (_, chart) -> chart },
        onRetryClick = onRetryMainTokenWithChartClick,
      ) {
        PriceChart(points = it, modifier = Modifier.fillMaxSize().padding(tabElementPadding))
      }
    }

    Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
      TokensSearchBar(
        state = referenceSearchBarState,
        tokensListState = referenceSearchListState,
        tokens = referenceSearchTokens,
        modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
        onQueryChange = onReferenceQueryChange,
        onActiveChange = onReferenceActiveChange,
        onTokenSelected = onReferenceTokenSelected,
      )

      LoadableView(
        modifier = Modifier.fillMaxSize(),
        loadable1 = mainTokenWithChart.map { (token) -> token },
        loadable2 = referenceToken,
        onRetryClick1 = onRetryMainTokenWithChartClick,
        onRetryClick2 = onRetryReferenceTokenClick,
      ) { mainToken, selectedReferenceToken ->
        TokenComparison(
          mainToken = mainToken,
          referenceToken = selectedReferenceToken,
          modifier = Modifier.fillMaxSize(),
        )
      }
    }
  }
}
