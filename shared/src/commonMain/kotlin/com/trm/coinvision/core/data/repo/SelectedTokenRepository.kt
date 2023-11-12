package com.trm.coinvision.core.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import com.trm.coinvision.core.network.client.CoinGeckoApiClient
import com.trm.coinvision.core.network.model.CoinResponse
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun selectedTokenRepository(
  client: CoinGeckoApiClient,
  dataStore: DataStore<Preferences>
): SelectedTokenRepository =
  object : SelectedTokenRepository {
    private val idKey = stringPreferencesKey("selected_token_id")
    private val nameKey = stringPreferencesKey("selected_token_name")
    private val imageKey = stringPreferencesKey("selected_token_image")

    override suspend fun updateSelectedToken(id: String, name: String, image: String?) {
      dataStore.edit {
        it[idKey] = id
        it[nameKey] = name
        it[imageKey] = image.orEmpty()
      }
    }

    override fun getSelectedTokenFlow(): Flow<SelectedToken> =
      dataStore.data.map {
        SelectedToken(
          id = it[idKey] ?: DEFAULT_SELECTED_TOKEN_ID,
          name = it[nameKey] ?: DEFAULT_SELECTED_TOKEN_NAME,
          image = it[imageKey] ?: DEFAULT_SELECTED_TOKEN_IMAGE
        )
      }

    override fun getSelectedTokenIdFlow(): Flow<String> =
      dataStore.data.map { it[idKey] ?: DEFAULT_SELECTED_TOKEN_ID }

    override suspend fun getTokenById(id: String): Result<TokenDTO> = runCatching {
      client.getTokenById(id).body<CoinResponse>()
    }
  }

private const val DEFAULT_SELECTED_TOKEN_ID = "ethereum"
private const val DEFAULT_SELECTED_TOKEN_NAME = "Ethereum"
private const val DEFAULT_SELECTED_TOKEN_IMAGE =
  "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1696501628"
