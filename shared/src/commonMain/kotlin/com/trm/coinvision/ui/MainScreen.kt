package com.trm.coinvision.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.core.common.util.LocalHeightSizeClass
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.compareTokens.CompareTokensTab
import com.trm.coinvision.ui.portfolio.PortfolioTab
import com.trm.coinvision.ui.tokensList.TokensListTab

internal object MainScreen : Screen {
  @Composable
  override fun Content() {
    TabNavigator(tab = CompareTokensTab) { tabNavigator ->
      Row {
        if (!usingNavigationBar) {
          NavigationRail {
            Spacer(Modifier.weight(1f))
            TabNavigationRailItem(CompareTokensTab)
            TabNavigationRailItem(TokensListTab)
            TabNavigationRailItem(PortfolioTab)
            Spacer(Modifier.weight(1f))
          }
        }

        Scaffold(
          bottomBar = {
            if (usingNavigationBar) {
              NavigationBar {
                TabNavigationBarItem(CompareTokensTab)
                TabNavigationBarItem(TokensListTab)
                TabNavigationBarItem(PortfolioTab)
              }
            }
          }
        ) { paddingValues ->
          Box(modifier = Modifier.padding(paddingValues)) {
            Crossfade(tabNavigator.current) {
              tabNavigator.saveableState(CURRENT_TAB_SAVE_STATE_KEY, it) {
                when (it) {
                  CompareTokensTab -> CompareTokensTab.Content()
                  TokensListTab -> TokensListTab.Content()
                  PortfolioTab -> PortfolioTab.Content()
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

private val usingNavigationBar
  @Composable
  get() =
    LocalWidthSizeClass.current == WindowWidthSizeClass.Compact ||
      LocalHeightSizeClass.current == WindowHeightSizeClass.Medium ||
      LocalHeightSizeClass.current == WindowHeightSizeClass.Expanded

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
