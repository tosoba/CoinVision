package com.trm.coinvision.ui

import com.trm.coinvision.core.domain.repo.TokenRepository
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import com.trm.coinvision.ui.compareTokens.CompareTokensScreenModel
import com.trm.coinvision.ui.tokensList.TokensListScreenModel
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarType
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val uiModule = module {
  factory { CompareTokensScreenModel(get(), get()) }
  factory { TokensListScreenModel(get(), get()) }
  factory { MainNavigatorScreenModel() }

  factory(named(TokensSearchBarType.MAIN)) { (coroutineScope: CoroutineScope) ->
    val tokenRepository = get<TokenRepository>()
    TokensSearchBarViewModel(
      coroutineScope = coroutineScope,
      getSelectedTokenFlow = tokenRepository::getSelectedMainTokenFlow,
      updateSelectedToken = tokenRepository::updateSelectedMainToken,
      getTokenListPaging = get<TokenListPagingRepository>()::invoke
    )
  }

  factory(named(TokensSearchBarType.REFERENCE)) { (coroutineScope: CoroutineScope) ->
    val tokenRepository = get<TokenRepository>()
    TokensSearchBarViewModel(
      coroutineScope = coroutineScope,
      getSelectedTokenFlow = tokenRepository::getSelectedReferenceTokenFlow,
      updateSelectedToken = tokenRepository::updateSelectedReferenceToken,
      getTokenListPaging = get<TokenListPagingRepository>()::invoke
    )
  }
}
