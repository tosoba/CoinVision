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
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.trm.coinvision.ui.compare.CompareTab
import com.trm.coinvision.ui.list.ListTab
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
  MaterialTheme {
    TabNavigator(tab = CompareTab) {
      Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(MR.strings.app_title)) }) },
        bottomBar = {
          NavigationBar {
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
  NavigationBarItem(
    selected = tabNavigator.current.key == tab.key,
    onClick = { tabNavigator.current = tab },
    icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } },
    label = { Text(tab.options.title) }
  )
}
