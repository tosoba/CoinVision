package com.trm.coinvision.core.domain.usecase

import kotlinx.coroutines.flow.MutableSharedFlow

class TokensSearchBarActiveChangeFlowUseCase(
  private val flow: MutableSharedFlow<Boolean> = MutableSharedFlow(),
) : MutableSharedFlow<Boolean> by flow {
  suspend operator fun invoke(active: Boolean) {
    flow.emit(active)
  }
}
