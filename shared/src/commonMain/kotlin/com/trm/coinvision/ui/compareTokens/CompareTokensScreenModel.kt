package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import com.trm.coinvision.core.domain.usecase.MainSearchBarSizeFlowUseCase
import kotlinx.coroutines.flow.asSharedFlow

class CompareTokensScreenModel(
  mainSearchBarSizeFlowUseCase: MainSearchBarSizeFlowUseCase,
) : ScreenModel {
  val mainSearchBarSizeFlow = mainSearchBarSizeFlowUseCase.asSharedFlow()
}
