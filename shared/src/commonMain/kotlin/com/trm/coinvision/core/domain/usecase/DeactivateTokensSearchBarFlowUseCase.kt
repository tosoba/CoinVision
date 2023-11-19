package com.trm.coinvision.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class DeactivateTokensSearchBarFlowUseCase(
  private val tokensSearchBarActiveChangeFlowUseCase: TokensSearchBarActiveChangeFlowUseCase,
) {
  operator fun invoke(): Flow<Unit> = tokensSearchBarActiveChangeFlowUseCase.filter { it }.map {}
}
