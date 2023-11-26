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
    private val mainTokenIdKey = stringPreferencesKey("selected_main_token_id")
    private val mainTokenSymbolKey = stringPreferencesKey("selected_main_token_symbol")
    private val mainTokenNameKey = stringPreferencesKey("selected_main_token_name")
    private val mainTokenImageKey = stringPreferencesKey("selected_main_token_image")

    override suspend fun updateSelectedMainToken(token: SelectedToken) {
      dataStore.edit {
        it[mainTokenIdKey] = token.id
        it[mainTokenSymbolKey] = token.symbol
        it[mainTokenNameKey] = token.name
        it[mainTokenImageKey] = token.image.orEmpty()
      }
    }

    override fun getSelectedMainTokenFlow(): Flow<SelectedToken> =
      dataStore.data.map { it.mapToSelectedMainToken() }

    override fun getSelectedMainTokenIdFlow(): Flow<String> =
      dataStore.data.map { it[mainTokenIdKey] ?: DEFAULT_SELECTED_MAIN_TOKEN_ID }

    private fun Preferences.mapToSelectedMainToken(): SelectedToken =
      SelectedToken(
        id = this[mainTokenIdKey] ?: DEFAULT_SELECTED_MAIN_TOKEN_ID,
        symbol = this[mainTokenSymbolKey] ?: DEFAULT_SELECTED_MAIN_TOKEN_SYMBOL,
        name = this[mainTokenNameKey] ?: DEFAULT_SELECTED_MAIN_TOKEN_NAME,
        image = this[mainTokenImageKey] ?: DEFAULT_SELECTED_MAIN_TOKEN_IMAGE
      )

    private val referenceTokenIdKey = stringPreferencesKey("selected_reference_token_id")
    private val referenceTokenSymbolKey = stringPreferencesKey("selected_reference_token_symbol")
    private val referenceTokenNameKey = stringPreferencesKey("selected_reference_token_name")
    private val referenceTokenImageKey = stringPreferencesKey("selected_reference_token_image")

    override suspend fun updateSelectedReferenceToken(token: SelectedToken) {
      dataStore.edit {
        it[referenceTokenIdKey] = token.id
        it[referenceTokenSymbolKey] = token.symbol
        it[referenceTokenNameKey] = token.name
        it[referenceTokenImageKey] = token.image.orEmpty()
      }
    }

    override fun getSelectedReferenceTokenFlow(): Flow<SelectedToken> =
      dataStore.data.map { it.mapToSelectedReferenceToken() }

    override fun getSelectedReferenceTokenIdFlow(): Flow<String> =
      dataStore.data.map { it[referenceTokenIdKey] ?: DEFAULT_SELECTED_REFERENCE_TOKEN_ID }

    private fun Preferences.mapToSelectedReferenceToken(): SelectedToken =
      SelectedToken(
        id = this[referenceTokenIdKey] ?: DEFAULT_SELECTED_REFERENCE_TOKEN_ID,
        symbol = this[referenceTokenSymbolKey] ?: DEFAULT_SELECTED_REFERENCE_TOKEN_SYMBOL,
        name = this[referenceTokenNameKey] ?: DEFAULT_SELECTED_REFERENCE_TOKEN_NAME,
        image = this[referenceTokenImageKey] ?: DEFAULT_SELECTED_REFERENCE_TOKEN_IMAGE
      )

    override suspend fun getTokenById(id: String): TokenDTO =
      client.getTokenById(id).body<CoinResponse>()
  }

private const val DEFAULT_SELECTED_MAIN_TOKEN_ID = "ethereum"
private const val DEFAULT_SELECTED_MAIN_TOKEN_SYMBOL = "ETH"
private const val DEFAULT_SELECTED_MAIN_TOKEN_NAME = "Ethereum"
private const val DEFAULT_SELECTED_MAIN_TOKEN_IMAGE =
  "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1696501628"

private fun defaultSelectedMainToken(): SelectedToken =
  SelectedToken(
    id = DEFAULT_SELECTED_MAIN_TOKEN_ID,
    symbol = DEFAULT_SELECTED_MAIN_TOKEN_SYMBOL,
    name = DEFAULT_SELECTED_MAIN_TOKEN_NAME,
    image = DEFAULT_SELECTED_MAIN_TOKEN_IMAGE
  )

private const val DEFAULT_SELECTED_REFERENCE_TOKEN_ID = "bitcoin"
private const val DEFAULT_SELECTED_REFERENCE_TOKEN_SYMBOL = "BTC"
private const val DEFAULT_SELECTED_REFERENCE_TOKEN_NAME = "Bitcoin"
private const val DEFAULT_SELECTED_REFERENCE_TOKEN_IMAGE =
  "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1696501400"

private fun defaultSelectedReferenceToken(): SelectedToken =
  SelectedToken(
    id = DEFAULT_SELECTED_REFERENCE_TOKEN_ID,
    symbol = DEFAULT_SELECTED_REFERENCE_TOKEN_SYMBOL,
    name = DEFAULT_SELECTED_REFERENCE_TOKEN_NAME,
    image = DEFAULT_SELECTED_REFERENCE_TOKEN_IMAGE
  )
