package com.trm.coinvision.core.domain.repo

import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.coroutines.flow.Flow

internal interface SelectedTokenRepository {
  suspend fun updateSelectedMainToken(token: SelectedToken)

  fun getSelectedMainTokenFlow(): Flow<SelectedToken>

  fun getSelectedMainTokenIdFlow(): Flow<String>

  suspend fun updateSelectedReferenceToken(token: SelectedToken)

  fun getSelectedReferenceTokenFlow(): Flow<SelectedToken>

  fun getSelectedReferenceTokenIdFlow(): Flow<String>

  suspend fun getTokenById(id: String): TokenDTO
}
