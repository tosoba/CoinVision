package com.trm.coinvision.core.data

import androidx.annotation.IntRange
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.repo.CryptoRepository
import com.trm.coinvision.core.network.client.COIN_GECKO_API_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.appendPathSegments

internal class CryptoNetworkRepository(private val coinGeckoClient: HttpClient) : CryptoRepository {
  private val FiatCurrency.queryParam: String
    get() = name.lowercase()

  override suspend fun getCoinMarkets(
    vsFiatCurrency: FiatCurrency,
    page: Int,
    perPage: Int,
    ids: List<String>?,
    order: String,
    sparkline: Boolean,
    locale: String,
    @IntRange(from = 0L, to = 18L) precision: Short
  ): HttpResponse =
    coinGeckoClient.get(COIN_GECKO_API_BASE_URL) {
      url {
        appendPathSegments("coins", "markets")
        parameters.append("vs_currency", vsFiatCurrency.queryParam)
        parameters.append("page", page.toString())
        parameters.append("per_page", perPage.toString())
        ids?.let { parameters.append("ids", it.joinToString(separator = ",")) }
        parameters.append("order", order)
        parameters.append("sparkline", sparkline.toString())
        parameters.append("locale", locale)
        parameters.append("precision", precision.toString())
      }
    }

  override suspend fun search(query: String): HttpResponse =
    coinGeckoClient.get(COIN_GECKO_API_BASE_URL) {
      url {
        appendPathSegments("search")
        parameters.append("query", query)
      }
    }
}
