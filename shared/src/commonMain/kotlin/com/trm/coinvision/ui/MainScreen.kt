package com.trm.coinvision.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.common.TokensSearchBar
import com.trm.coinvision.ui.common.TokensSearchBarState
import com.trm.coinvision.ui.common.rememberTokensSearchBarState
import com.trm.coinvision.ui.compareTokens.CompareTokensTab
import com.trm.coinvision.ui.tokensList.TokensListTab

internal object MainScreen : Screen {
  @Composable
  override fun Content() {
    TabNavigator(tab = CompareTokensTab) { tabNavigator ->
      Row {
        if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
          NavigationRail {
            Spacer(Modifier.weight(1f))
            TabNavigationRailItem(CompareTokensTab)
            TabNavigationRailItem(TokensListTab)
            Spacer(Modifier.weight(1f))
          }
        }

        var scaffoldHeightPx by remember { mutableStateOf(0) }
        var bottomBarHeightPx by remember { mutableStateOf(0) }

        val screenModel = getScreenModel<MainScreenModel>()
        val mainTokensListState = rememberLazyListState()
        val mainTokensSearchBarState = rememberTokensSearchBarState {
          TokensSearchBarState(
            onActiveChange = screenModel::onActiveChange,
            onQueryChange = screenModel::onQueryChange,
            onSuggestionSelected = { screenModel.onSuggestionSelected() }
          )
        }
        val coinMarkets = screenModel.coinMarkets.collectAsLazyPagingItems()

        Scaffold(
          modifier = Modifier.onGloballyPositioned { scaffoldHeightPx = it.size.height },
          topBar = {
            if (LocalWidthSizeClass.current == WindowWidthSizeClass.Compact) {
              TokensSearchBar(
                modifier =
                  Modifier.fillMaxWidth()
                    .fillMainSearchBarMaxHeight(
                      scaffoldHeightPx = scaffoldHeightPx,
                      bottomBarHeightPx = bottomBarHeightPx
                    )
                    .padding(mainSearchBarPadding),
                searchBarState = mainTokensSearchBarState,
                tokensListState = mainTokensListState,
                tokens = coinMarkets
              )
            }
          },
          bottomBar = {
            if (LocalWidthSizeClass.current == WindowWidthSizeClass.Compact) {
              NavigationBar(
                modifier = Modifier.onGloballyPositioned { bottomBarHeightPx = it.size.height },
              ) {
                TabNavigationBarItem(CompareTokensTab)
                TabNavigationBarItem(TokensListTab)
              }
            }
          }
        ) { paddingValues ->
          Box(modifier = Modifier.padding(paddingValues)) {
            if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
              TokensSearchBar(
                modifier =
                  Modifier.fillMaxWidth(.5f)
                    .fillMainSearchBarMaxHeight(
                      scaffoldHeightPx = scaffoldHeightPx,
                      bottomBarHeightPx = bottomBarHeightPx
                    )
                    .padding(mainSearchBarPadding),
                searchBarState = mainTokensSearchBarState,
                tokensListState = mainTokensListState,
                tokens = coinMarkets
              )
            }
            Crossfade(tabNavigator.current) {
              tabNavigator.saveableState(CURRENT_TAB_SAVE_STATE_KEY, it) {
                when (it) {
                  CompareTokensTab -> CompareTokensTab.Content()
                  TokensListTab -> TokensListTab.Content()
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun Modifier.fillMainSearchBarMaxHeight(
  scaffoldHeightPx: Int,
  bottomBarHeightPx: Int
): Modifier =
  then(
    Modifier.heightIn(
      max =
        with(LocalDensity.current) {
          (scaffoldHeightPx - bottomBarHeightPx).toDouble().times(0.9).toInt().toDp()
        }
    )
  )

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
