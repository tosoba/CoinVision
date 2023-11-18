package com.trm.coinvision.core.domain

import com.trm.coinvision.core.domain.usecase.GetSelectedMainTokenFlowUseCase
import org.koin.dsl.module

internal val domainModule = module { factory { GetSelectedMainTokenFlowUseCase(get()) } }
