package com.trm.coinvision.core.domain.usecase

import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.MutableSharedFlow

class MainSearchBarSizeFlowUseCase(
  private val flow: MutableSharedFlow<IntSize?> = MutableSharedFlow(),
) : MutableSharedFlow<IntSize?> by flow
