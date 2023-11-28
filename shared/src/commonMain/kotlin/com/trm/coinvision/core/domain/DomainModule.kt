package com.trm.coinvision.core.domain

import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenWithChartFlowUseCase
import com.trm.coinvision.core.domain.usecase.GetSelectedReferenceTokenFlowUseCase
import org.koin.dsl.module

internal val domainModule = module {
  factory { GetSelectedMainTokenWithChartFlowUseCase(get()) }
  factory { GetSelectedReferenceTokenFlowUseCase(get()) }
}
