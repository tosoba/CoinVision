package com.trm.coinvision.ui

import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
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
  factory { MainScreenModel() }

  factory(named(TokensSearchBarType.MAIN)) { (coroutineScope: CoroutineScope) ->
    val selectedTokenRepository = get<SelectedTokenRepository>()
    TokensSearchBarViewModel(
      coroutineScope = coroutineScope,
      getSelectedToken = selectedTokenRepository::getSelectedMainToken,
      updateSelectedToken = selectedTokenRepository::updateSelectedMainToken,
      getTokenListPaging = get<TokenListPagingRepository>()::invoke
    )
  }

  factory(named(TokensSearchBarType.REFERENCE)) { (coroutineScope: CoroutineScope) ->
    val selectedTokenRepository = get<SelectedTokenRepository>()
    TokensSearchBarViewModel(
      coroutineScope = coroutineScope,
      getSelectedToken = selectedTokenRepository::getSelectedReferenceToken,
      updateSelectedToken = selectedTokenRepository::updateSelectedReferenceToken,
      getTokenListPaging = get<TokenListPagingRepository>()::invoke
    )
  }
}
