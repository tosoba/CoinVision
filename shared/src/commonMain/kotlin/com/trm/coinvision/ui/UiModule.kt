package com.trm.coinvision.ui

import com.trm.coinvision.ui.tokensList.TokensListScreenModel
import org.koin.dsl.module

internal val uiModule = module {
  factory { TokensListScreenModel(get()) }
  factory { MainScreenModel() }
}
