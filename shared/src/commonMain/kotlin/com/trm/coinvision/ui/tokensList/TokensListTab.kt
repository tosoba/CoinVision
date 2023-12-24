package com.trm.coinvision.ui.tokensList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.ext.root
import com.trm.coinvision.core.common.util.ext.toMarketCapFormat
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.ui.MainNavigatorScreenModel
import com.trm.coinvision.ui.chart.PriceChart
import com.trm.coinvision.ui.common.CoinVisionProgressIndicator
import com.trm.coinvision.ui.common.CoinVisionRetryColumn
import com.trm.coinvision.ui.common.CoinVisionRetryRow
import com.trm.coinvision.ui.common.LoadableView
import com.trm.coinvision.ui.common.SegmentedButton
import com.trm.coinvision.ui.common.SingleLineAutoSizeText
import com.trm.coinvision.ui.common.errorText
import com.trm.coinvision.ui.common.usingHorizontalTabSplit
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.tabElementPadding
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object TokensListTab : Tab {
  @OptIn(ExperimentalVoyagerApi::class)
  @Composable
  override fun Content() {
    val mainTokensSearchBarViewModel =
      LocalNavigator.currentOrThrow
        .root()
        .getNavigatorScreenModel<MainNavigatorScreenModel>()
        .mainTokensSearchBarViewModel
    val tokensListScreenModel = getScreenModel<TokensListScreenModel>()

    val mainToken by tokensListScreenModel.mainTokenFlow.collectAsState()
    val listState = rememberLazyListState()
    val tokenPotentialComparisonItems =
      tokensListScreenModel.tokenPotentialComparisonPagingFlow.collectAsLazyPagingItems()

    if (usingHorizontalTabSplit) {
      val chartPoints by tokensListScreenModel.mainTokenChartPointsFlow.collectAsState()
      val chartPeriod by tokensListScreenModel.chartPeriod.collectAsState()

      Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
            viewModel = mainTokensSearchBarViewModel
          )

          SegmentedButton(
            modifier =
              Modifier.fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = tabElementPadding),
            items = MarketChartDaysPeriod.entries.toList(),
            selectedItem = chartPeriod,
            label = MarketChartDaysPeriod::label,
            onItemClick = tokensListScreenModel::onChartPeriodClick
          )

          LoadableView(
            modifier = Modifier.fillMaxSize().padding(tabElementPadding),
            loadable = chartPoints,
            onRetryClick = tokensListScreenModel::onRetryMainTokenWithChartClick
          ) {
            PriceChart(modifier = Modifier.fillMaxSize(), points = it)
          }
        }

        LoadableView(
          modifier = Modifier.weight(.5f).fillMaxHeight(),
          loadable = mainToken,
          onRetryClick = tokensListScreenModel::onRetryMainTokenWithChartClick
        ) {
          TokenPotentialComparisonLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            comparisonItems = tokenPotentialComparisonItems
          )
        }
      }
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        TokensSearchBar(
          modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
          viewModel = mainTokensSearchBarViewModel
        )

        LoadableView(
          modifier = Modifier.fillMaxSize(),
          loadable = mainToken,
          onRetryClick = tokensListScreenModel::onRetryMainTokenWithChartClick
        ) {
          TokenPotentialComparisonLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            comparisonItems = tokenPotentialComparisonItems,
          )
        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TokenPotentialComparisonLazyColumn(
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  comparisonItems: LazyPagingItems<TokenPotentialComparison> =
    flowOf(PagingData.empty<TokenPotentialComparison>()).collectAsLazyPagingItems(),
) {
  LazyColumn(modifier = modifier, contentPadding = PaddingValues(10.dp), state = state) {
    when (val prepend = comparisonItems.loadState.prepend) {
      is LoadState.Error -> {
        item {
          CoinVisionRetryRow(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            text = prepend.error.errorText(),
            onRetryClick = comparisonItems::retry
          )
        }
      }
      LoadState.Loading -> {
        item { CoinVisionProgressIndicator(modifier = Modifier.padding(20.dp)) }
      }
      else -> {}
    }

    when (val refresh = comparisonItems.loadState.refresh) {
      is LoadState.Error -> {
        item {
          CoinVisionRetryColumn(
            modifier = Modifier.fillParentMaxSize(),
            text = refresh.error.errorText(),
            onRetryClick = comparisonItems::retry
          )
        }
      }
      LoadState.Loading -> {
        item { CoinVisionProgressIndicator(modifier = Modifier.fillParentMaxSize()) }
      }
      is LoadState.NotLoading -> {
        comparisonItems
          .takeIf { it.itemCount > 0 }
          ?.get(0)
          ?.potential
          ?.token
          ?.symbol
          ?.let {
            stickyHeader {
              TokenPotentialComparisonHeader(
                modifier =
                  Modifier.fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(bottom = 5.dp, start = 5.dp, end = 5.dp),
                tokenSymbol = it
              )
            }
          }

        items(
          count = comparisonItems.itemCount,
          key = comparisonItems.itemKey { it.referenceToken.id }
        ) { index ->
          comparisonItems[index]?.let {
            TokenPotentialComparisonItem(
              modifier = Modifier.fillMaxWidth().padding(5.dp),
              index = index,
              item = it
            )
          }
        }
      }
    }

    when (val append = comparisonItems.loadState.append) {
      is LoadState.Error -> {
        item {
          CoinVisionRetryRow(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            text = append.error.errorText(),
            onRetryClick = comparisonItems::retry
          )
        }
      }
      LoadState.Loading -> {
        item { CoinVisionProgressIndicator(modifier = Modifier.padding(20.dp)) }
      }
      else -> {}
    }
  }
}

@Composable
private fun TokenPotentialComparisonHeader(modifier: Modifier = Modifier, tokenSymbol: String) {
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    SingleLineAutoSizeText(
      modifier = Modifier.weight(1f),
      text = "With market cap of:",
      style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.width(10.dp))

    SingleLineAutoSizeText(
      modifier = Modifier.weight(1f),
      text = "Potential ${tokenSymbol.uppercase()} price:",
      style = MaterialTheme.typography.headlineMedium
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TokenPotentialComparisonItem(
  modifier: Modifier = Modifier,
  index: Int,
  item: TokenPotentialComparison
) {
  val (subjectToken, potential) = item
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(5.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(5.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = index.toString())

        Spacer(modifier = Modifier.width(5.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(modifier = Modifier.basicMarquee(), text = subjectToken.symbol.uppercase())
          Text(modifier = Modifier.basicMarquee(), text = subjectToken.name)
        }

        Spacer(modifier = Modifier.width(5.dp))

        Text(text = subjectToken.marketCap?.toMarketCapFormat().orEmpty())
      }

      potential?.let { (_, potentialPriceFormatted, potentialUpsideFormatted) ->
        Spacer(modifier = Modifier.width(5.dp))

        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
          Text(text = potentialPriceFormatted)
          potentialUpsideFormatted?.let { Text(text = it) }
        }
      }
    }
  }
}
