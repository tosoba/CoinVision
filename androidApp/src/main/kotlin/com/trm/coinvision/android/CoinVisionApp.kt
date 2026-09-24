package com.trm.coinvision.android

import android.app.Application
import com.trm.coinvision.PlatformKoinInitializer
import com.trm.coinvision.core.common.util.initNapierDebug

class CoinVisionApp : Application() {
  override fun onCreate() {
    super.onCreate()
    PlatformKoinInitializer(this)()
    initNapierDebug()
  }
}
