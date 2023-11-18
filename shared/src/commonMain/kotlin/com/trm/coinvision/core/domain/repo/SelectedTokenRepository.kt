package com.trm.coinvision.core.domain.repo

import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenDTO
import kotlinx.coroutines.flow.Flow

internal interface SelectedTokenRepository {
  suspend fun updateSelectedMainToken(token: SelectedToken)

  suspend fun getSelectedMainToken(): SelectedToken

  fun getSelectedMainTokenIdFlow(): Flow<String>

  suspend fun updateSelectedReferenceToken(token: SelectedToken)

  suspend fun getSelectedReferenceToken(): SelectedToken

  fun getSelectedReferenceTokenIdFlow(): Flow<String>

  suspend fun getTokenById(id: String): TokenDTO
}
