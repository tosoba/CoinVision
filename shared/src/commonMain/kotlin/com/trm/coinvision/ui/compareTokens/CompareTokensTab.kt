package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.trm.coinvision.ui.common.SelectedTokenData
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.tokensSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object CompareTokensTab : Tab {
  @OptIn(ExperimentalVoyagerApi::class)
  @Composable
  override fun Content() {
    val mainTokensSearchBarViewModel =
      LocalNavigator.currentOrThrow
        .root()
        .getNavigatorScreenModel<MainNavigatorScreenModel>()
        .mainTokensSearchBarViewModel
    val compareTokensScreenModel = getScreenModel<CompareTokensScreenModel>()

    val selectedMainToken by compareTokensScreenModel.selectedMainTokenFlow.collectAsState()
    val selectedReferenceToken by
      compareTokensScreenModel.selectedReferenceTokenFlow.collectAsState()

    if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
      Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
            viewModel = mainTokensSearchBarViewModel
          )
          PriceChart(
            modifier = Modifier.fillMaxSize().padding(10.dp),
          )
        }

        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBar(
            modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
            viewModel = compareTokensScreenModel.referenceTokensSearchBarViewModel
          )
          SelectedTokenData(
            modifier = Modifier.fillMaxSize(),
            token = selectedReferenceToken,
          )
        }
      }
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        TokensSearchBar(
          modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
          viewModel = mainTokensSearchBarViewModel
        )
        PriceChart(
          modifier = Modifier.fillMaxWidth().weight(.5f).padding(10.dp),
        )

        TokensSearchBar(
          modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding),
          viewModel = compareTokensScreenModel.referenceTokensSearchBarViewModel
        )
        SelectedTokenData(
          modifier = Modifier.fillMaxWidth().weight(.5f),
          token = selectedReferenceToken
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
