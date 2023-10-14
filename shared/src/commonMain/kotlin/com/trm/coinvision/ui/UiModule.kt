package com.trm.coinvision.ui

import com.trm.coinvision.ui.tokensList.tokensListModule
import org.koin.dsl.module

val uiModule = module { includes(tokensListModule) }
