package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import com.trm.coinvision.core.domain.usecase.SelectedTokenFlowUseCase

internal class CompareTokensScreenModel(
  private val selectedTokenFlowUseCase: SelectedTokenFlowUseCase
) : ScreenModel {}
