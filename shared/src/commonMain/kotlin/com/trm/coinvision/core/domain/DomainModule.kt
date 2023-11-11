package com.trm.coinvision.core.domain

import com.trm.coinvision.core.domain.usecase.GetTokensPagingUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedTokenFlowUseCase
import org.koin.dsl.module

internal val domainModule = module {
  factory { GetTokensPagingUseCase(get()) }
  single { GetSelectedTokenFlowUseCase(get()) }
}
