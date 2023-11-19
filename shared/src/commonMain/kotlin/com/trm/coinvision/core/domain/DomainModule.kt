package com.trm.coinvision.core.domain

import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedReferenceTokenFlowUseCase
import org.koin.dsl.module

internal val domainModule = module {
  factory { GetSelectedMainTokenFlowUseCase(get()) }
  factory { GetSelectedReferenceTokenFlowUseCase(get()) }
}
