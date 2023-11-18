package com.trm.coinvision.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.common.rememberTokensSearchBarState
import com.trm.coinvision.ui.compareTokens.CompareTokensTab
import com.trm.coinvision.ui.tokensList.TokensListTab

internal object MainScreen : Screen {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<MainScreenModel>()

    val initialTokenSearchBarState by screenModel.initialTokenSearchBarStateFlow.collectAsState()
    val tokensSearchBarState =
      rememberTokensSearchBarState(initialTokenSearchBarState) { initialTokenSearchBarState }

    val tokensListState = rememberLazyListState()
    val tokens = screenModel.searchBarTokensPagingFlow.collectAsLazyPagingItems()

    val compareTokensTab =
      remember(tokensSearchBarState, tokens, tokensListState) {
        CompareTokensTab(
          searchBarState = tokensSearchBarState,
          tokensListState = tokensListState,
          tokens = tokens,
          onQueryChange = screenModel::onQueryChange,
          onActiveChange = { screenModel.onActiveChange() },
          onTokenSelected = screenModel::onTokenSelected
        )
      }

    val tokensListTab =
      remember(tokensSearchBarState, tokens, tokensListState) {
        TokensListTab(
          searchBarState = tokensSearchBarState,
          tokensListState = tokensListState,
          tokens = tokens,
          onQueryChange = screenModel::onQueryChange,
          onActiveChange = { screenModel.onActiveChange() },
          onTokenSelected = screenModel::onTokenSelected
        )
      }

    TabNavigator(tab = compareTokensTab) { tabNavigator ->
      Row {
        if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
          NavigationRail {
            Spacer(Modifier.weight(1f))
            TabNavigationRailItem(compareTokensTab)
            TabNavigationRailItem(tokensListTab)
            Spacer(Modifier.weight(1f))
          }
        }

        Scaffold(
          bottomBar = {
            if (LocalWidthSizeClass.current == WindowWidthSizeClass.Compact) {
              NavigationBar {
                TabNavigationBarItem(compareTokensTab)
                TabNavigationBarItem(tokensListTab)
              }
            }
          }
        ) { paddingValues ->
          Box(modifier = Modifier.padding(paddingValues)) {
            Crossfade(tabNavigator.current.key) {
              tabNavigator.saveableState(
                key = CURRENT_TAB_SAVE_STATE_KEY,
                tab =
                  when (it) {
                    compareTokensTab.key -> compareTokensTab
                    tokensListTab.key -> tokensListTab
                    else -> throw IllegalStateException()
                  }
              ) {
                when (it) {
                  compareTokensTab.key -> compareTokensTab.Content()
                  tokensListTab.key -> tokensListTab.Content()
                }
              }
            }
          }
        }
      }
    }
  }
}

private const val CURRENT_TAB_SAVE_STATE_KEY = "currentTab"

@Composable
private fun RowScope.TabNavigationBarItem(tab: Tab) {
  val tabNavigator = LocalTabNavigator.current
  NavigationBarItem(
    selected = tabNavigator.current.key == tab.key,
    onClick = { tabNavigator.current = tab },
    icon = { TabNavigationItemIcon(tab) },
    label = { Text(tab.options.title) }
  )
}

@Composable
private fun TabNavigationRailItem(tab: Tab) {
  val tabNavigator = LocalTabNavigator.current
  NavigationRailItem(
    selected = tabNavigator.current.key == tab.key,
    onClick = { tabNavigator.current = tab },
    icon = { TabNavigationItemIcon(tab) },
    label = { Text(tab.options.title) }
  )
}

@Composable
private fun TabNavigationItemIcon(tab: Tab) {
  tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) }
}

internal val mainSearchBarPadding = 10.dp
internal val mainSearchBarHeight = 56.dp
