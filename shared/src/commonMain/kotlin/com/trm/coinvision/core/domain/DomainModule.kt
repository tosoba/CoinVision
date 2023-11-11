package com.trm.coinvision.core.domain

import com.trm.coinvision.core.domain.usecase.GetTokensPagingUseCase
import com.trm.coinvision.core.domain.usecase.SelectedTokenFlowUseCase
import org.koin.dsl.module

internal val domainModule = module {
  factory { GetTokensPagingUseCase(get()) }
  single { SelectedTokenFlowUseCase() }
}
