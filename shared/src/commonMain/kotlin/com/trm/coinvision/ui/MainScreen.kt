package com.trm.coinvision.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.compare
import coinvision.shared.generated.resources.list
import com.trm.coinvision.ui.common.usingNavigationBar
import com.trm.coinvision.ui.compareTokens.CompareTokensRoute
import com.trm.coinvision.ui.tokensList.TokensListRoute
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MainScreen() {
  val mainNavigatorViewModel: MainNavigatorViewModel = koinViewModel()
  val selectedTab = mainNavigatorViewModel.selectedTab
  val pagerState = rememberPagerState(initialPage = selectedTab.ordinal) { MainTab.entries.size }

  LaunchedEffect(pagerState) {
    snapshotFlow(pagerState::settledPage).collect { page ->
      val tab = MainTab.entries[page]
      if (tab != mainNavigatorViewModel.selectedTab) {
        mainNavigatorViewModel.onTabSelected(tab)
      }
    }
  }

  LaunchedEffect(selectedTab) {
    if (pagerState.settledPage != selectedTab.ordinal) {
      pagerState.animateScrollToPage(selectedTab.ordinal)
    }
  }

  Row {
    if (!usingNavigationBar) {
      NavigationRail {
        Spacer(Modifier.weight(1f))
        MainTabNavigationRailItem(tab = MainTab.COMPARE_TOKENS, viewModel = mainNavigatorViewModel)
        MainTabNavigationRailItem(tab = MainTab.TOKENS_LIST, viewModel = mainNavigatorViewModel)
        Spacer(Modifier.weight(1f))
      }
    }

    Scaffold(
      bottomBar = {
        if (usingNavigationBar) {
          NavigationBar {
            MainTabNavigationBarItem(
              tab = MainTab.COMPARE_TOKENS,
              viewModel = mainNavigatorViewModel,
            )
            MainTabNavigationBarItem(tab = MainTab.TOKENS_LIST, viewModel = mainNavigatorViewModel)
          }
        }
      }
    ) { paddingValues ->
      HorizontalPager(state = pagerState, modifier = Modifier.padding(paddingValues)) { page ->
        when (MainTab.entries[page]) {
          MainTab.COMPARE_TOKENS -> CompareTokensRoute()
          MainTab.TOKENS_LIST -> TokensListRoute()
        }
      }
    }
  }
}

@Composable
private fun mainTabTitle(tab: MainTab): String =
  when (tab) {
    MainTab.COMPARE_TOKENS -> stringResource(Res.string.compare)
    MainTab.TOKENS_LIST -> stringResource(Res.string.list)
  }

@Composable
private fun mainTabIcon(tab: MainTab): Painter =
  when (tab) {
    MainTab.COMPARE_TOKENS -> painterResource(Res.drawable.compare)
    MainTab.TOKENS_LIST -> painterResource(Res.drawable.list)
  }

@Composable
private fun RowScope.MainTabNavigationBarItem(
  tab: MainTab,
  viewModel: MainNavigatorViewModel,
) {
  NavigationBarItem(
    selected = viewModel.selectedTab == tab,
    onClick = { viewModel.onTabSelected(tab) },
    icon = { Icon(painter = mainTabIcon(tab), contentDescription = mainTabTitle(tab)) },
    label = { Text(mainTabTitle(tab)) },
  )
}

@Composable
private fun MainTabNavigationRailItem(
  tab: MainTab,
  viewModel: MainNavigatorViewModel,
) {
  NavigationRailItem(
    selected = viewModel.selectedTab == tab,
    onClick = { viewModel.onTabSelected(tab) },
    icon = { Icon(painter = mainTabIcon(tab), contentDescription = mainTabTitle(tab)) },
    label = { Text(mainTabTitle(tab)) },
  )
}
