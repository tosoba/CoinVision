package com.trm.coinvision

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.PlatformLocaleChangedObserverEffect
import com.trm.coinvision.core.common.util.stringResources
import com.trm.coinvision.ui.compareTokens.CompareTokensTab
import com.trm.coinvision.ui.tokensList.TokensListTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
  var stringResources by remember { mutableStateOf(stringResources()) }
  PlatformLocaleChangedObserverEffect { stringResources = stringResources(it) }

  CompositionLocalProvider(LocalStringResources provides stringResources) {
    MaterialTheme {
      TabNavigator(tab = CompareTokensTab) {
        Scaffold(
          topBar = {
            CenterAlignedTopAppBar(title = { Text(LocalStringResources.current.appTitle) })
          },
          bottomBar = {
            NavigationBar {
              TabNavigationItem(CompareTokensTab)
              TabNavigationItem(TokensListTab)
            }
          }
        ) {
          CurrentTab()
        }
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
