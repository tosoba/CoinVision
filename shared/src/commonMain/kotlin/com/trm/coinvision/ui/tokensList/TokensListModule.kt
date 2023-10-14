package com.trm.coinvision.ui.tokensList

import org.koin.dsl.module

internal val tokensListModule = module { factory { TokensListScreenModel(get()) } }
