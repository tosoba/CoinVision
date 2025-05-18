package com.trm.coinvision

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.trm.coinvision.core.common.util.initNapierDebug
import com.trm.coinvision.ui.chart.ComposeSharedFactory
import com.trm.coinvision.ui.chart.LocalSharedFactory
import platform.UIKit.UIViewController

fun MainViewController(generatedViewFactory: ComposeSharedFactory): UIViewController =
  ComposeUIViewController(
    configure = {
      PlatformKoinInitializer()()
      initNapierDebug()
    }
  ) {
    CompositionLocalProvider(LocalSharedFactory provides generatedViewFactory) { App() }
  }
