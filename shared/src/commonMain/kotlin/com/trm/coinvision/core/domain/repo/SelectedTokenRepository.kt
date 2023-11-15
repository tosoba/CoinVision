package com.trm.coinvision.core.domain.repo

import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.coroutines.flow.Flow

internal interface SelectedTokenRepository {
  suspend fun updateSelectedToken(token: SelectedToken)

  suspend fun getSelectedToken(): SelectedToken

  fun getSelectedTokenFlow(): Flow<SelectedToken>

  fun getSelectedTokenIdFlow(): Flow<String>

  suspend fun getTokenById(id: String): TokenDTO
}
