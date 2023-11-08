package com.trm.coinvision.ui

import com.trm.coinvision.ui.compareTokens.CompareTokensScreenModel
import com.trm.coinvision.ui.tokensList.TokensListScreenModel
import org.koin.dsl.module

internal val uiModule = module {
  factory { CompareTokensScreenModel(get()) }
  factory { TokensListScreenModel(get(), get()) }
  factory { MainScreenModel(get(), get()) }
}
