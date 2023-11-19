package com.trm.coinvision.core.domain

import com.trm.coinvision.core.domain.model.TokensSearchBarType
import com.trm.coinvision.core.domain.usecase.DeactivateTokensSearchBarFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedReferenceTokenFlowUseCase
import com.trm.coinvision.core.domain.usecase.TokensSearchBarActiveChangeFlowUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val domainModule = module {
  factory { GetSelectedMainTokenFlowUseCase(get()) }
  factory { GetSelectedReferenceTokenFlowUseCase(get()) }

  single(named(TokensSearchBarType.MAIN)) { TokensSearchBarActiveChangeFlowUseCase() }
  single(named(TokensSearchBarType.REFERENCE)) { TokensSearchBarActiveChangeFlowUseCase() }

  factory(named(TokensSearchBarType.MAIN)) {
    DeactivateTokensSearchBarFlowUseCase(
      tokensSearchBarActiveChangeFlowUseCase = get(named(TokensSearchBarType.REFERENCE))
    )
  }
  factory(named(TokensSearchBarType.REFERENCE)) {
    DeactivateTokensSearchBarFlowUseCase(
      tokensSearchBarActiveChangeFlowUseCase = get(named(TokensSearchBarType.MAIN))
    )
  }
}
