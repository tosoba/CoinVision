package com.trm.coinvision.core.domain.repo

import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.coroutines.flow.Flow

internal interface SelectedTokenRepository {
  suspend fun updateSelectedToken(id: String)

  fun getSelectedTokenFlow(): Flow<Result<TokenDTO>>
}
