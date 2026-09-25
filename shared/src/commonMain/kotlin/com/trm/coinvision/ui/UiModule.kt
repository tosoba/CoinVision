package com.trm.coinvision.ui

import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import com.trm.coinvision.core.domain.repo.TokenRepository
import com.trm.coinvision.ui.compareTokens.CompareTokensViewModel
import com.trm.coinvision.ui.tokensList.TokensListViewModel
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarType
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val uiModule = module {
  viewModel { MainNavigatorViewModel() }

  viewModel {
    val tokenRepository = get<TokenRepository>()
    CompareTokensViewModel(
      getSelectedMainTokenWithChartFlowUseCase = get(),
      getSelectedReferenceTokenFlowUseCase = get(),
      swapSelectedTokens = tokenRepository::swapSelectedTokens,
      updateChartPeriod = tokenRepository::updateChartPeriod,
      getChartPeriodFlow = tokenRepository::getChartPeriodFlow
    )
  }

  viewModel {
    val tokenRepository = get<TokenRepository>()
    TokensListViewModel(
      tokenListPagingRepository = get(),
      getSelectedMainTokenWithChartFlowUseCase = get(),
      updateChartPeriod = tokenRepository::updateChartPeriod,
      getChartPeriodFlow = tokenRepository::getChartPeriodFlow
    )
  }

  viewModel(named(TokensSearchBarType.MAIN)) {
    val tokenRepository = get<TokenRepository>()
    TokensSearchBarViewModel(
      getSelectedTokenFlow = tokenRepository::getSelectedMainTokenFlow,
      updateSelectedToken = tokenRepository::updateSelectedMainToken,
      getTokenListPaging = get<TokenListPagingRepository>()::invoke
    )
  }

  viewModel(named(TokensSearchBarType.REFERENCE)) {
    val tokenRepository = get<TokenRepository>()
    TokensSearchBarViewModel(
      getSelectedTokenFlow = tokenRepository::getSelectedReferenceTokenFlow,
      updateSelectedToken = tokenRepository::updateSelectedReferenceToken,
      getTokenListPaging = get<TokenListPagingRepository>()::invoke
    )
  }
}
