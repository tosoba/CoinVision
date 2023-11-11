package com.trm.coinvision.core.domain.usecase

import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.repo.CryptoRepository
import kotlinx.coroutines.flow.MutableSharedFlow

internal class GetSelectedTokenFlowUseCase(
  private val repository: CryptoRepository,
  private val resultFlow: MutableSharedFlow<TokenListItemDTO> = MutableSharedFlow(replay = 1),
) : MutableSharedFlow<TokenListItemDTO> by resultFlow {
  private val idFlow: MutableSharedFlow<String> = MutableSharedFlow()

  init {
    // TODO: fetch last selected main search bar token id from db and fetch it
  }
}
