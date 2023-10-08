package com.trm.coinvision.android

import android.app.Application
import com.trm.coinvision.ui.tokensList.tokensListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CoinVisionApp : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@CoinVisionApp)
      modules(tokensListModule)
    }
  }
}
