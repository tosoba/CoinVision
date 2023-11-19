package com.trm.coinvision.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

internal class MainScreenModel : ScreenModel, KoinComponent {
  val mainTokensSearchBarViewModel: TokensSearchBarViewModel by
    inject(named(TokensSearchBarType.MAIN)) { parametersOf(coroutineScope) }
}
