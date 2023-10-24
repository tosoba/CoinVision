package com.trm.coinvision.ui

import com.trm.coinvision.ui.tokensList.tokensListModule
import org.koin.dsl.module

internal val uiModule = module { includes(tokensListModule) }
