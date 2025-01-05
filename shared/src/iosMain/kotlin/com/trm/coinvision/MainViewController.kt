package com.trm.coinvision

import androidx.compose.ui.window.ComposeUIViewController
import com.trm.coinvision.core.common.util.initNapierDebug
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController =
  ComposeUIViewController(
    configure = {
      PlatformKoinInitializer()()
      initNapierDebug()
    }
  ) {
    App()
  }
