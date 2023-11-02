package com.trm.coinvision

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.ui.TopSearchBar
import com.trm.coinvision.ui.compareTokens.CompareTokensTab
import com.trm.coinvision.ui.tokensList.TokensListTab

internal object MainScreen : Screen {
  @Composable
  override fun Content() {
    TabNavigator(tab = CompareTokensTab) {
      Scaffold(
        topBar = {
          Box(modifier = Modifier.fillMaxWidth()) {
            TopSearchBar(modifier = Modifier.align(Alignment.Center).padding(top = 10.dp))
          }
        },
        bottomBar = {
          NavigationBar {
            TabNavigationItem(CompareTokensTab)
            TabNavigationItem(TokensListTab)
          }
        }
      ) {
        Box(modifier = Modifier.padding(it)) { CurrentTab() }
      }
    }
  }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
  val tabNavigator = LocalTabNavigator.current
  NavigationBarItem(
    selected = tabNavigator.current.key == tab.key,
    onClick = { tabNavigator.current = tab },
    icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } },
    label = { Text(tab.options.title) }
  )
}
