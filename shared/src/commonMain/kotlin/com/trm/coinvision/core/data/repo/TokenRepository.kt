package com.trm.coinvision.core.data.repo

import com.trm.coinvision.core.database.CoinVisionDatabase
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.model.MarketChartDTO
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.repo.TokenRepository
import com.trm.coinvision.core.network.client.CoinGeckoApiClient
import com.trm.coinvision.core.network.model.CoinResponse
import com.trm.coinvision.core.network.model.MarketChartResponse
import io.ktor.client.call.body
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal fun tokenRepository(
  client: CoinGeckoApiClient,
  database: CoinVisionDatabase
): TokenRepository =
  object : TokenRepository {
    override suspend fun updateSelectedMainToken(token: SelectedToken) {
      database.insertSelectedMainToken(token)
    }

    override fun getSelectedMainTokenFlow(): Flow<SelectedToken> =
      database.selectMostRecentMainTokenFlow().map { it ?: defaultMainToken() }

    override fun getSelectedMainTokenIdWithChartPeriodFlow():
      Flow<Pair<String, MarketChartDaysPeriod>> =
      database.selectMostRecentMainTokenIdWithChartPeriodFlow().map {
        it ?: (DEFAULT_SELECTED_MAIN_TOKEN_ID to MarketChartDaysPeriod.default)
      }

    override suspend fun updateSelectedReferenceToken(token: SelectedToken) {
      database.insertSelectedReferenceToken(token)
    }

    override fun getSelectedReferenceTokenFlow(): Flow<SelectedToken> =
      database.selectMostRecentReferenceTokenFlow().map { it ?: defaultReferenceToken() }

    override fun getSelectedReferenceTokenIdFlow(): Flow<String> =
      database.selectMostRecentReferenceTokenFlow().map {
        it?.id ?: DEFAULT_SELECTED_REFERENCE_TOKEN_ID
      }

    override suspend fun swapSelectedTokens() {
      coroutineScope {
        val mainToken = async { database.selectMostRecentMainToken() ?: defaultMainToken() }
        val referenceToken = async {
          database.selectMostRecentReferenceToken() ?: defaultReferenceToken()
        }
        launch { database.insertSelectedMainToken(referenceToken.await()) }
        launch { database.insertSelectedReferenceToken(mainToken.await()) }
      }
    }

    override fun getChartPeriodFlow(): Flow<MarketChartDaysPeriod> =
      database.selectChartPeriodFlow().map { it ?: MarketChartDaysPeriod.default }

    override suspend fun getTokenById(id: String): TokenDTO =
      client.getTokenById(id).body<CoinResponse>()

    override suspend fun getTokenChart(
      id: String,
      vsFiatCurrency: FiatCurrency,
      days: MarketChartDaysPeriod
    ): MarketChartDTO =
      client
        .getMarketChart(id = id, vsFiatCurrency = vsFiatCurrency, days = days)
        .body<MarketChartResponse>()
  }

private const val DEFAULT_SELECTED_MAIN_TOKEN_ID = "ethereum"
private const val DEFAULT_SELECTED_MAIN_TOKEN_SYMBOL = "ETH"
private const val DEFAULT_SELECTED_MAIN_TOKEN_NAME = "Ethereum"
private const val DEFAULT_SELECTED_MAIN_TOKEN_IMAGE =
  "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1696501628"

private fun defaultMainToken(): SelectedToken =
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

private fun defaultReferenceToken(): SelectedToken =
  SelectedToken(
    id = DEFAULT_SELECTED_REFERENCE_TOKEN_ID,
    symbol = DEFAULT_SELECTED_REFERENCE_TOKEN_SYMBOL,
    name = DEFAULT_SELECTED_REFERENCE_TOKEN_NAME,
    image = DEFAULT_SELECTED_REFERENCE_TOKEN_IMAGE
  )
