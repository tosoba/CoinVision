package com.trm.coinvision.core.domain.usecase

import com.trm.coinvision.core.domain.model.TokenListItem
import kotlinx.coroutines.flow.MutableSharedFlow

internal class SelectedTokenFlowUseCase(
  private val flow: MutableSharedFlow<TokenListItem> = MutableSharedFlow(replay = 1),
) : MutableSharedFlow<TokenListItem> by flow
