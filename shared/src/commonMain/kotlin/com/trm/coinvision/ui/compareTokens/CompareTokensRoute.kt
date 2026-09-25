package com.trm.coinvision.ui.compareTokens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.swap_horizontal
import coinvision.shared.generated.resources.swap_tokens
import coinvision.shared.generated.resources.swap_vertical
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.chart.PriceChart
import com.trm.coinvision.ui.chart.PriceChartHeader
import com.trm.coinvision.ui.common.LoadableView
import com.trm.coinvision.ui.common.TokenComparison
import com.trm.coinvision.ui.common.usingHorizontalTabSplit
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
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

  Box(modifier = Modifier.fillMaxSize()) {
    val chartPeriod by viewModel.chartPeriod.collectAsState()
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
            onChartPeriodClick = viewModel::onChartPeriodClick,
          )

          LoadableView(
            modifier = Modifier.fillMaxSize(),
            loadable = mainTokenWithChart.map { (_, chart) -> chart },
            onRetryClick = viewModel::onRetryMainTokenWithChartClick,
          ) {
            PriceChart(modifier = Modifier.fillMaxSize().padding(tabElementPadding), points = it)
          }
        }

        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
            viewModel = referenceTokensSearchBarViewModel,
          )

          LoadableView(
            modifier = Modifier.fillMaxSize(),
            loadable1 = mainTokenWithChart.map { (token) -> token },
            loadable2 = selectedReferenceToken,
            onRetryClick1 = viewModel::onRetryMainTokenWithChartClick,
            onRetryClick2 = viewModel::onRetryReferenceTokenClick,
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
          onChartPeriodClick = viewModel::onChartPeriodClick,
        )

        LoadableView(
          modifier = Modifier.fillMaxWidth().weight(.5f).padding(tabElementPadding),
          loadable = mainTokenWithChart.map { (_, chart) -> chart },
          onRetryClick = viewModel::onRetryMainTokenWithChartClick,
        ) {
          PriceChart(modifier = Modifier.fillMaxSize(), points = it)
        }

        TokensSearchBar(
          modifier = Modifier.fillMaxWidth().padding(tabElementPadding),
          viewModel = referenceTokensSearchBarViewModel,
        )

        LoadableView(
          modifier = Modifier.fillMaxWidth().weight(.5f).padding(tabElementPadding),
          loadable1 = mainTokenWithChart.map { (token) -> token },
          loadable2 = selectedReferenceToken,
          onRetryClick1 = viewModel::onRetryMainTokenWithChartClick,
          onRetryClick2 = viewModel::onRetryReferenceTokenClick,
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
