package com.trm.coinvision

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.ui.compare.CompareTab
import com.trm.coinvision.ui.list.ListTab

@Composable
fun App() {
  MaterialTheme {
    TabNavigator(CompareTab) {
      Scaffold(
        topBar = { TopAppBar { Text("CoinVision") } },
        bottomBar = {
          BottomNavigation {
            TabNavigationItem(CompareTab)
            TabNavigationItem(ListTab)
          }
        }
      ) {
        CurrentTab()
      }
    }
  }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
  val tabNavigator = LocalTabNavigator.current
  BottomNavigationItem(
    selected = tabNavigator.current.key == tab.key,
    onClick = { tabNavigator.current = tab },
    icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } },
    label = { Text(tab.options.title) }
  )
}
