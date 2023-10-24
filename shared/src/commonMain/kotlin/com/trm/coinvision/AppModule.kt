package com.trm.coinvision

import com.trm.coinvision.core.coreModule
import com.trm.coinvision.ui.uiModule
import org.koin.dsl.module

val appModule = module { includes(uiModule, coreModule) }

interface KoinInitializer {
  operator fun invoke()
}

expect class PlatformKoinInitializer : KoinInitializer
