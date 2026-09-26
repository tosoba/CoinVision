package com.trm.coinvision

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.coinvision.ui.chart.ComposeSharedFactory
import com.trm.coinvision.ui.chart.LocalComposeSharedFactory
import platform.UIKit.UIViewController

@OptIn(ExperimentalComposeUiApi::class)
fun compareTokensViewController(composeSharedFactory: ComposeSharedFactory): UIViewController =
  ComposeUIViewController(configure = { opaque = false }) {
    CompositionLocalProvider(LocalComposeSharedFactory provides composeSharedFactory) {
      CompareTokensTab()
    }
  }

@OptIn(ExperimentalComposeUiApi::class)
fun tokensListViewController(composeSharedFactory: ComposeSharedFactory): UIViewController =
  ComposeUIViewController(configure = { opaque = false }) {
    CompositionLocalProvider(LocalComposeSharedFactory provides composeSharedFactory) {
      TokensListTab()
    }
  }
