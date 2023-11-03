package com.trm.coinvision.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.compareTokens.CompareTokensTab
import com.trm.coinvision.ui.tokensList.TokensListTab

internal object MainScreen : Screen {
  @Composable
  override fun Content() {
    TabNavigator(tab = CompareTokensTab) {
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

        Scaffold(
          modifier = Modifier.onGloballyPositioned { scaffoldHeightPx = it.size.height },
          topBar = {
            MainSearchBar(
              modifier =
                Modifier.fillMaxWidth()
                  .padding(10.dp)
                  .heightIn(
                    max =
                      with(LocalDensity.current) {
                        (scaffoldHeightPx - bottomBarHeightPx).toDouble().times(0.9).toInt().toDp()
                      }
                  )
            )
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
        ) {
          Box(modifier = Modifier.padding(it)) { CurrentTab() }
        }
      }
    }
  }
}

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
