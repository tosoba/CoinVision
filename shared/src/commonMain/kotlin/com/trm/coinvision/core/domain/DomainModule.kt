package com.trm.coinvision.core.domain

import com.trm.coinvision.core.domain.usecase.GetSelectedTokenFlowUseCase
import org.koin.dsl.module

internal val domainModule = module { factory { GetSelectedTokenFlowUseCase(get()) } }
