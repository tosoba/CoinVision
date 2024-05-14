package com.trm.coinvision

import androidx.compose.ui.window.ComposeUIViewController
import com.trm.coinvision.core.common.util.initNapierDebug

fun MainViewController() =
  ComposeUIViewController(
    configure = {
      PlatformKoinInitializer()()
      initNapierDebug()
    }
  ) {
    App()
  }
