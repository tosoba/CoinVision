package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.common.SelectedTokenData
import com.trm.coinvision.ui.common.TokensSearchBar
import com.trm.coinvision.ui.common.TokensSearchBarVerticalSpacer
import com.trm.coinvision.ui.common.rememberTokensSearchBarState
import com.trm.coinvision.ui.mainSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal object CompareTokensTab : Tab {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<CompareTokensScreenModel>()
    val mainSelectedToken by screenModel.mainSelectedTokenFlow.collectAsState()

    val initialTokenSearchBarState by screenModel.initialTokenSearchBarStateFlow.collectAsState()
    val tokensSearchBarState =
      rememberTokensSearchBarState(initialTokenSearchBarState) { initialTokenSearchBarState }

    val tokensListState = rememberLazyListState()
    val tokens = screenModel.searchBarTokensPagingFlow.collectAsLazyPagingItems()

    @Composable
    fun TokensSearchBar() {
      TokensSearchBar(
        modifier = Modifier.fillMaxWidth().padding(mainSearchBarPadding),
        searchBarState = tokensSearchBarState,
        tokensListState = tokensListState,
        tokens = tokens,
        onQueryChange = screenModel::onQueryChange,
        onActiveChange = { screenModel.onActiveChange() },
        onTokenSelected = screenModel::onTokenSelected
      )
    }

    if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
      Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBarVerticalSpacer()
          SelectedTokenData(modifier = Modifier.fillMaxSize(), token = mainSelectedToken)
        }
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBar()
          SelectedTokenData(modifier = Modifier.fillMaxSize(), token = mainSelectedToken)
        }
      }
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        TokensSearchBarVerticalSpacer()
        SelectedTokenData(modifier = Modifier.fillMaxWidth().weight(.5f), token = mainSelectedToken)
        TokensSearchBar()
        SelectedTokenData(modifier = Modifier.fillMaxWidth().weight(.5f), token = mainSelectedToken)
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
