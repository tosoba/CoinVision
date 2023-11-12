package com.trm.coinvision.core.data.source

import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.coroutines.flow.MutableSharedFlow

internal class SelectedTokenResultInMemoryDataSource(
  private val flow: MutableSharedFlow<Result<TokenDTO>> = MutableSharedFlow(replay = 1),
) : MutableSharedFlow<Result<TokenDTO>> by flow
