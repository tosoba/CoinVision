package com.trm.coinvision.core.domain.usecase

import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.MutableSharedFlow

class MainSearchBarSizeFlowUseCase(
  private val flow: MutableSharedFlow<IntSize?> = MutableSharedFlow(replay = 1),
) : MutableSharedFlow<IntSize?> by flow
