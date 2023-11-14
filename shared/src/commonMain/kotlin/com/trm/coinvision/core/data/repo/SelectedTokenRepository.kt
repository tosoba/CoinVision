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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal fun selectedTokenRepository(
  client: CoinGeckoApiClient,
  dataStore: DataStore<Preferences>
): SelectedTokenRepository =
  object : SelectedTokenRepository {
    private val idKey = stringPreferencesKey("selected_token_id")
    private val symbolKey = stringPreferencesKey("selected_token_symbol")
    private val nameKey = stringPreferencesKey("selected_token_name")
    private val imageKey = stringPreferencesKey("selected_token_image")

    override suspend fun updateSelectedToken(token: SelectedToken) {
      dataStore.edit {
        it[idKey] = token.id
        it[symbolKey] = token.symbol
        it[nameKey] = token.name
        it[imageKey] = token.image.orEmpty()
      }
    }

    override suspend fun getSelectedToken(): SelectedToken =
      dataStore.data.firstOrNull()?.mapToSelectedToken() ?: defaultSelectedToken()

    override fun getSelectedTokenFlow(): Flow<SelectedToken> =
      dataStore.data.map { it.mapToSelectedToken() }

    override fun getSelectedTokenIdFlow(): Flow<String> =
      dataStore.data.map { it[idKey] ?: DEFAULT_SELECTED_TOKEN_ID }

    override suspend fun getTokenById(id: String): Result<TokenDTO> = runCatching {
      client.getTokenById(id).body<CoinResponse>()
    }

    private fun Preferences.mapToSelectedToken(): SelectedToken =
      SelectedToken(
        id = this[idKey] ?: DEFAULT_SELECTED_TOKEN_ID,
        symbol = this[symbolKey] ?: DEFAULT_SELECTED_TOKEN_SYMBOL,
        name = this[nameKey] ?: DEFAULT_SELECTED_TOKEN_NAME,
        image = this[imageKey] ?: DEFAULT_SELECTED_TOKEN_IMAGE
      )
  }

private const val DEFAULT_SELECTED_TOKEN_ID = "ethereum"
private const val DEFAULT_SELECTED_TOKEN_SYMBOL = "ETH"
private const val DEFAULT_SELECTED_TOKEN_NAME = "Ethereum"
private const val DEFAULT_SELECTED_TOKEN_IMAGE =
  "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1696501628"

private fun defaultSelectedToken(): SelectedToken =
  SelectedToken(
    id = DEFAULT_SELECTED_TOKEN_ID,
    symbol = DEFAULT_SELECTED_TOKEN_SYMBOL,
    name = DEFAULT_SELECTED_TOKEN_NAME,
    image = DEFAULT_SELECTED_TOKEN_IMAGE
  )
