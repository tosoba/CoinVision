package com.trm.coinvision.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.compare
import coinvision.shared.generated.resources.list
import com.trm.coinvision.ui.common.usingNavigationBar
import com.trm.coinvision.ui.compareTokens.CompareTokensRoute
import com.trm.coinvision.ui.tokensList.TokensListRoute
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MainScreen() {
  val scope = rememberCoroutineScope()
  val pagerState =
    rememberPagerState(
      initialPage = MainTab.COMPARE_TOKENS.ordinal,
      pageCount = MainTab.entries::size,
    )
  val selectedTab = MainTab.entries[pagerState.currentPage]

  fun switchTabTo(tab: MainTab) {
    scope.launch { pagerState.scrollToPage(tab.ordinal) }
  }

  Scaffold(
    bottomBar = {
      if (usingNavigationBar) {
        NavigationBar {
          MainTabNavigationBarItem(
            tab = MainTab.COMPARE_TOKENS,
            selectedTab = selectedTab,
            onTabSelected = {
              switchTabTo(MainTab.COMPARE_TOKENS)
            },
          )
          MainTabNavigationBarItem(
            tab = MainTab.TOKENS_LIST,
            selectedTab = selectedTab,
            onTabSelected = {
              switchTabTo(MainTab.TOKENS_LIST)
            },
          )
        }
      }
    }
  ) { paddingValues ->
    Row(modifier = Modifier.padding(paddingValues)) {
      if (!usingNavigationBar) {
        NavigationRail(windowInsets = WindowInsets()) {
          MainTabNavigationRailItem(
            tab = MainTab.COMPARE_TOKENS,
            selectedTab = selectedTab,
            onTabSelected = {
              switchTabTo(MainTab.COMPARE_TOKENS)
            },
          )

          MainTabNavigationRailItem(
            tab = MainTab.TOKENS_LIST,
            selectedTab = selectedTab,
            onTabSelected = {
              switchTabTo(MainTab.TOKENS_LIST)
            },
          )
        }
      }

      HorizontalPager(
        state = pagerState,
        modifier = Modifier.weight(1f),
        userScrollEnabled = false,
      ) { page ->
        when (MainTab.entries[page]) {
          MainTab.COMPARE_TOKENS -> CompareTokensRoute()
          MainTab.TOKENS_LIST -> TokensListRoute()
        }
      }
    }
  }
}

@Composable
private fun RowScope.MainTabNavigationBarItem(
  tab: MainTab,
  selectedTab: MainTab,
  onTabSelected: () -> Unit,
) {
  NavigationBarItem(
    selected = selectedTab == tab,
    onClick = onTabSelected,
    icon = { MainTabIcon(tab) },
    label = { Text(mainTabTitle(tab)) },
  )
}

@Composable
private fun MainTabNavigationRailItem(
  tab: MainTab,
  selectedTab: MainTab,
  onTabSelected: () -> Unit,
) {
  NavigationRailItem(
    selected = selectedTab == tab,
    onClick = onTabSelected,
    icon = { MainTabIcon(tab) },
    label = { Text(mainTabTitle(tab)) },
  )
}

@Composable
private fun MainTabIcon(tab: MainTab) {
  Icon(painter = mainTabIcon(tab), contentDescription = mainTabTitle(tab))
}

@Composable
private fun mainTabTitle(tab: MainTab): String =
  stringResource(
    when (tab) {
      MainTab.COMPARE_TOKENS -> Res.string.compare
      MainTab.TOKENS_LIST -> Res.string.list
    }
  )

@Composable
private fun mainTabIcon(tab: MainTab): Painter =
  painterResource(
    when (tab) {
      MainTab.COMPARE_TOKENS -> Res.drawable.compare
      MainTab.TOKENS_LIST -> Res.drawable.list
    }
  )
