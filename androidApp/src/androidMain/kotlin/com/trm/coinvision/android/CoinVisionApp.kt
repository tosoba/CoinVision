package com.trm.coinvision.android

import android.app.Application
import com.trm.coinvision.PlatformKoinInitializer

class CoinVisionApp : Application() {
  override fun onCreate() {
    super.onCreate()
    PlatformKoinInitializer(this)()
  }
}
