package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.core.common.util.ext.root
import com.trm.coinvision.ui.MainNavigatorScreenModel
import com.trm.coinvision.ui.chart.PriceChart
import com.trm.coinvision.ui.common.LoadableView
import com.trm.coinvision.ui.common.SelectedTokenData
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.tokensSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

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
      if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
        Row(modifier = Modifier.fillMaxSize()) {
          Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
            TokensSearchBar(
              modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
              viewModel = mainTokensSearchBarViewModel
            )
            LoadableView(
              modifier = Modifier.fillMaxSize(),
              loadable = mainTokenWithChart.map { (_, chart) -> chart },
              onRetryClick = compareTokensScreenModel::onRetryMainTokenWithChartClick
            ) {
              PriceChart(modifier = Modifier.fillMaxSize().padding(10.dp), points = it)
            }
          }

          Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
            TokensSearchBar(
              modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
              viewModel = compareTokensScreenModel.referenceTokensSearchBarViewModel
            )
            LoadableView(
              modifier = Modifier.fillMaxSize(),
              loadable = selectedReferenceToken,
              onRetryClick = compareTokensScreenModel::onRetryReferenceTokenClick
            ) {
              SelectedTokenData(modifier = Modifier.fillMaxSize(), token = it)
            }
          }
        }
      } else {
        Column(modifier = Modifier.fillMaxSize()) {
          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
            viewModel = mainTokensSearchBarViewModel
          )

          LoadableView(
            modifier = Modifier.fillMaxWidth().weight(.5f),
            loadable = mainTokenWithChart.map { (_, chart) -> chart },
            onRetryClick = compareTokensScreenModel::onRetryMainTokenWithChartClick
          ) {
            PriceChart(modifier = Modifier.fillMaxSize().padding(10.dp), points = it)
          }

          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
            viewModel = compareTokensScreenModel.referenceTokensSearchBarViewModel
          )
          LoadableView(
            modifier = Modifier.fillMaxWidth().weight(.5f),
            loadable = selectedReferenceToken,
            onRetryClick = compareTokensScreenModel::onRetryReferenceTokenClick
          ) {
            SelectedTokenData(modifier = Modifier.fillMaxSize(), token = it)
          }
        }
      }

      FloatingActionButton(
        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
        onClick = compareTokensScreenModel::onSwapTokensClick
      ) {
        Image(
          painter =
            painterResource(
              if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) "swap_horizontal.xml"
              else "swap_vertical.xml"
            ),
          contentDescription = null
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
        title = LocalStringResources.current.compare,
        icon = painterResource("compare.xml")
      )
}
