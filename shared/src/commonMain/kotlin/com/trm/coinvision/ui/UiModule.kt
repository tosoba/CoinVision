package com.trm.coinvision.ui

import com.trm.coinvision.core.domain.model.TokensSearchBarType
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import com.trm.coinvision.ui.compareTokens.CompareTokensScreenModel
import com.trm.coinvision.ui.tokensList.TokensListScreenModel
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
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
      getSelectedTokenUseCase = selectedTokenRepository::getSelectedMainToken,
      updateSelectedTokenUseCase = selectedTokenRepository::updateSelectedMainToken,
      getTokenListPagingUseCase = get<TokenListPagingRepository>()::invoke,
      deactivateSearchBarFlowUseCase = get(named(TokensSearchBarType.MAIN)),
      searchBarActiveChangeFlowUseCase = get(named(TokensSearchBarType.MAIN))
    )
  }

  factory(named(TokensSearchBarType.REFERENCE)) { (coroutineScope: CoroutineScope) ->
    val selectedTokenRepository = get<SelectedTokenRepository>()
    TokensSearchBarViewModel(
      coroutineScope = coroutineScope,
      getSelectedTokenUseCase = selectedTokenRepository::getSelectedReferenceToken,
      updateSelectedTokenUseCase = selectedTokenRepository::updateSelectedReferenceToken,
      getTokenListPagingUseCase = get<TokenListPagingRepository>()::invoke,
      deactivateSearchBarFlowUseCase = get(named(TokensSearchBarType.REFERENCE)),
      searchBarActiveChangeFlowUseCase = get(named(TokensSearchBarType.REFERENCE))
    )
  }
}
