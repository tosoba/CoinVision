package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.compare
import coinvision.shared.generated.resources.swap_horizontal
import coinvision.shared.generated.resources.swap_vertical
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.core.common.util.ext.root
import com.trm.coinvision.ui.MainNavigatorScreenModel
import com.trm.coinvision.ui.chart.PriceChart
import com.trm.coinvision.ui.chart.PriceChartHeader
import com.trm.coinvision.ui.common.LoadableView
import com.trm.coinvision.ui.common.TokenComparison
import com.trm.coinvision.ui.common.usingHorizontalTabSplit
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.tabElementPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object CompareTokensTab : Tab {
  @OptIn(ExperimentalVoyagerApi::class, ExperimentalResourceApi::class)
  @Composable
  override fun Content() {
    val mainTokensSearchBarViewModel =
      LocalNavigator.currentOrThrow
        .root()
        .getNavigatorScreenModel<MainNavigatorScreenModel>()
        .mainTokensSearchBarViewModel
    val compareTokensScreenModel = getScreenModel<CompareTokensScreenModel>()

    val mainTokenWithChart by compareTokensScreenModel.mainTokenWithChartFlow.collectAsState()
    val selectedReferenceToken by compareTokensScreenModel.referenceTokenFlow.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
      val chartPeriod by compareTokensScreenModel.chartPeriod.collectAsState()
      val chartPeriodButtonScrollState = rememberScrollState()

      if (usingHorizontalTabSplit) {
        Row(modifier = Modifier.fillMaxSize()) {
          Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
            TokensSearchBar(
              modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
              viewModel = mainTokensSearchBarViewModel,
            )

            PriceChartHeader(
              modifier = Modifier.fillMaxWidth().padding(horizontal = tabElementPadding),
              daysPeriodScrollState = chartPeriodButtonScrollState,
              marketData = mainTokenWithChart.mapNullable { (token) -> token.marketData },
              chartPeriod = chartPeriod,
              onChartPeriodClick = compareTokensScreenModel::onChartPeriodClick,
            )

            LoadableView(
              modifier = Modifier.fillMaxSize(),
              loadable = mainTokenWithChart.map { (_, chart) -> chart },
              onRetryClick = compareTokensScreenModel::onRetryMainTokenWithChartClick,
            ) {
              PriceChart(modifier = Modifier.fillMaxSize().padding(tabElementPadding), points = it)
            }
          }

          Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
            TokensSearchBar(
              modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
              viewModel = compareTokensScreenModel.referenceTokensSearchBarViewModel,
            )

            LoadableView(
              modifier = Modifier.fillMaxSize(),
              loadable1 = mainTokenWithChart.map { (token) -> token },
              loadable2 = selectedReferenceToken,
              onRetryClick1 = compareTokensScreenModel::onRetryMainTokenWithChartClick,
              onRetryClick2 = compareTokensScreenModel::onRetryReferenceTokenClick,
            ) { mainToken, referenceToken ->
              TokenComparison(
                modifier = Modifier.fillMaxSize(),
                mainToken = mainToken,
                referenceToken = referenceToken,
              )
            }
          }
        }
      } else {
        Column(modifier = Modifier.fillMaxSize()) {
          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
            viewModel = mainTokensSearchBarViewModel,
          )

          PriceChartHeader(
            modifier =
              Modifier.fillMaxWidth()
                .horizontalScroll(chartPeriodButtonScrollState)
                .padding(horizontal = tabElementPadding),
            daysPeriodScrollState = chartPeriodButtonScrollState,
            marketData = mainTokenWithChart.mapNullable { (token) -> token.marketData },
            chartPeriod = chartPeriod,
            onChartPeriodClick = compareTokensScreenModel::onChartPeriodClick,
          )

          LoadableView(
            modifier = Modifier.fillMaxWidth().weight(.5f).padding(tabElementPadding),
            loadable = mainTokenWithChart.map { (_, chart) -> chart },
            onRetryClick = compareTokensScreenModel::onRetryMainTokenWithChartClick,
          ) {
            PriceChart(modifier = Modifier.fillMaxSize(), points = it)
          }

          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
            viewModel = compareTokensScreenModel.referenceTokensSearchBarViewModel,
          )

          LoadableView(
            modifier = Modifier.fillMaxWidth().weight(.5f).padding(tabElementPadding),
            loadable1 = mainTokenWithChart.map { (token) -> token },
            loadable2 = selectedReferenceToken,
            onRetryClick1 = compareTokensScreenModel::onRetryMainTokenWithChartClick,
            onRetryClick2 = compareTokensScreenModel::onRetryReferenceTokenClick,
          ) { mainToken, referenceToken ->
            TokenComparison(
              modifier = Modifier.fillMaxSize(),
              mainToken = mainToken,
              referenceToken = referenceToken,
            )
          }
        }
      }

      FloatingActionButton(
        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
        onClick = compareTokensScreenModel::onSwapTokensClick,
      ) {
        Image(
          painter =
            painterResource(
              if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
                Res.drawable.swap_horizontal
              } else {
                Res.drawable.swap_vertical
              }
            ),
          contentDescription = LocalStringResources.current.swapTokens,
        )
      }
    }
  }

  @OptIn(ExperimentalResourceApi::class)
  override val options: TabOptions
    @Composable
    get() =
      TabOptions(
        index = 0u,
        title = stringResource(Res.string.compare),
        icon = painterResource(Res.drawable.compare),
      )
}
