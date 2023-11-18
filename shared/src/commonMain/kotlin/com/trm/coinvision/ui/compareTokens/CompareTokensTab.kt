package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.ui.common.SelectedTokenData
import com.trm.coinvision.ui.common.TokensSearchBar
import com.trm.coinvision.ui.common.TokensSearchBarState
import com.trm.coinvision.ui.common.rememberTokensSearchBarState
import com.trm.coinvision.ui.mainSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal class CompareTokensTab(
  private val searchBarState: TokensSearchBarState,
  private val tokensListState: LazyListState,
  private val tokens: LazyPagingItems<TokenListItemDTO>,
  private val onQueryChange: (String) -> Unit,
  private val onActiveChange: (Boolean) -> Unit,
  private val onTokenSelected: (TokenListItemDTO) -> Unit
) : Tab {
  @Composable
  override fun Content() {
    @Composable
    fun MainTokensSearchBar() {
      TokensSearchBar(
        modifier = Modifier.fillMaxWidth().padding(mainSearchBarPadding),
        searchBarState = searchBarState,
        tokensListState = tokensListState,
        tokens = tokens,
        onQueryChange = onQueryChange,
        onActiveChange = onActiveChange,
        onTokenSelected = onTokenSelected
      )
    }

    val screenModel = getScreenModel<CompareTokensScreenModel>()

    val selectedMainToken by screenModel.selectedMainTokenFlow.collectAsState()
    val selectedReferenceToken by screenModel.selectedReferenceTokenFlow.collectAsState()

    val initialTokenSearchBarState by screenModel.initialTokenSearchBarStateFlow.collectAsState()
    val tokensSearchBarState =
      rememberTokensSearchBarState(initialTokenSearchBarState) { initialTokenSearchBarState }

    val tokensListState = rememberLazyListState()
    val tokens = screenModel.searchBarTokensPagingFlow.collectAsLazyPagingItems()

    @Composable
    fun ReferenceTokensSearchBar() {
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
          MainTokensSearchBar()
          SelectedTokenData(
            modifier = Modifier.fillMaxSize(),
            token = selectedMainToken,
          )
        }

        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          ReferenceTokensSearchBar()
          SelectedTokenData(
            modifier = Modifier.fillMaxSize(),
            token = selectedReferenceToken,
          )
        }
      }
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        MainTokensSearchBar()
        SelectedTokenData(
          modifier = Modifier.fillMaxWidth().weight(.5f),
          token = selectedMainToken,
        )

        ReferenceTokensSearchBar()
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
