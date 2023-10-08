package com.trm.coinvision.ui.tokensList

import org.koin.dsl.module

val tokensListModule = module { factory { TokensListScreenModel() } }
