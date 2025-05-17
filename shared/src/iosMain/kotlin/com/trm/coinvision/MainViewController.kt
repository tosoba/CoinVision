package com.trm.coinvision

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.compose.swift.bridge.ComposeSharedFactory
import co.touchlab.compose.swift.bridge.LocalSharedFactory
import com.trm.coinvision.core.common.util.initNapierDebug
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
