package com.trm.coinvision.ui

import androidx.compose.ui.unit.IntSize
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.usecase.MainSearchBarSizeFlowUseCase
import kotlinx.coroutines.launch

class MainScreenModel(
  private val mainSearchBarSizeFlowUseCase: MainSearchBarSizeFlowUseCase,
) : ScreenModel {
  fun onMainSearchBarSizeChanged(size: IntSize?) {
    coroutineScope.launch { mainSearchBarSizeFlowUseCase.emit(size) }
  }
}
