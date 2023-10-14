package com.trm.coinvision.core.data

import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.network.client.COIN_GECKO_API_BASE_URL
import com.trm.coinvision.core.network.model.CoinMarketsResponseItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

class CryptoNetworkRepository(private val coinGeckoClient: HttpClient) {
  private val FiatCurrency.queryParam: String
    get() = name.lowercase()

  suspend fun getCoinMarkets(vsFiatCurrency: FiatCurrency): List<CoinMarketsResponseItem> =
    coinGeckoClient
      .get(COIN_GECKO_API_BASE_URL) {
        url {
          appendPathSegments("coins", "markets")
          parameters.append("vs_currency", vsFiatCurrency.queryParam)
        }
      }
      .body()
}
