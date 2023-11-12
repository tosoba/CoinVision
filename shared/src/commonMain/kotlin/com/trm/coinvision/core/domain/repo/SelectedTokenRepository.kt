package com.trm.coinvision.core.domain.repo

import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.coroutines.flow.Flow

internal interface SelectedTokenRepository {
  suspend fun saveSelectedToken(id: String, name: String, image: String?)

  fun getSelectedTokenResultFlow(): Flow<Result<TokenDTO>>

  fun getSelectedTokenFlow(): Flow<SelectedToken>
}
