package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

internal class CompareTokensScreenModel(
  selectedTokenRepository: SelectedTokenRepository,
) : ScreenModel {
  val selectedToken: SharedFlow<Result<TokenDTO>> =
    selectedTokenRepository
      .getSelectedTokenResultFlow()
      .shareIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
      )
}
