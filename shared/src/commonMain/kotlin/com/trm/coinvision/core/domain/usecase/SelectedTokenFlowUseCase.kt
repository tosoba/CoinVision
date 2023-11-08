package com.trm.coinvision.core.domain.usecase

import com.trm.coinvision.core.domain.model.CoinMarketsItem
import kotlinx.coroutines.flow.MutableSharedFlow

internal class SelectedTokenFlowUseCase(
  private val flow: MutableSharedFlow<CoinMarketsItem> = MutableSharedFlow(),
) : MutableSharedFlow<CoinMarketsItem> by flow
