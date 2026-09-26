package com.trm.coinvision

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.coinvision.core.common.util.initNapierDebug
import com.trm.coinvision.ui.chart.ComposeSharedFactory
import com.trm.coinvision.ui.chart.LocalComposeSharedFactory
import platform.UIKit.UIViewController

fun mainViewController(composeSharedFactory: ComposeSharedFactory): UIViewController =
  ComposeUIViewController(
    configure = {
      PlatformKoinInitializer()()
      initNapierDebug()
    }
  ) {
    CompositionLocalProvider(LocalComposeSharedFactory provides composeSharedFactory) { App() }
  }
