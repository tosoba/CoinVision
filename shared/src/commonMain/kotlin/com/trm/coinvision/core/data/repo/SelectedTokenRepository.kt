package com.trm.coinvision.core.data.repo

import com.trm.coinvision.core.data.source.SelectedTokenInMemoryDataSource
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import com.trm.coinvision.core.network.client.CoinGeckoApiClient
import com.trm.coinvision.core.network.model.CoinResponse
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

internal fun selectedTokenRepository(
  client: CoinGeckoApiClient,
  selectedTokenDataSource: SelectedTokenInMemoryDataSource
): SelectedTokenRepository =
  object : SelectedTokenRepository {
    override suspend fun updateSelectedToken(id: String) {
      selectedTokenDataSource.emit(
        runCatching { client.getTokenById(id) }.map { it.body<CoinResponse>() }
      )
    }

    override fun getSelectedTokenFlow(): Flow<Result<TokenDTO>> = selectedTokenDataSource
  }
