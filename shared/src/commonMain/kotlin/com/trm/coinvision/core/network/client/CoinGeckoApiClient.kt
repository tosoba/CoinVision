package com.trm.coinvision.core.network.client

import androidx.annotation.IntRange
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.appendPathSegments

internal class CoinGeckoApiClient(private val client: HttpClient) {
  private val FiatCurrency.queryParam: String
    get() = name.lowercase()

  suspend fun getTokens(
    vsFiatCurrency: FiatCurrency,
    page: Int = 1,
    perPage: Int = 250,
    ids: List<String>? = null,
    order: String = "market_cap_desc",
    sparkline: Boolean = false,
    locale: String = "en",
    @IntRange(from = 0L, to = 18L) precision: Short = 2
  ): HttpResponse =
    client.get(COIN_GECKO_API_BASE_URL) {
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

  suspend fun search(query: String): HttpResponse =
    client.get(COIN_GECKO_API_BASE_URL) {
      url {
        appendPathSegments("search")
        parameters.append("query", query)
      }
    }

  suspend fun getTokenById(id: String): HttpResponse =
    client.get(COIN_GECKO_API_BASE_URL) {
      url {
        appendPathSegments("coins", id)
        parameters.append("localization", "false")
        parameters.append("tickers", "false")
        parameters.append("market_data", "true")
        parameters.append("community_data", "false")
        parameters.append("developer_data", "false")
        parameters.append("sparkline", "true")
      }
    }

  suspend fun getMarketChart(
    id: String,
    vsFiatCurrency: FiatCurrency,
    days: MarketChartDaysPeriod = MarketChartDaysPeriod.DAY
  ): HttpResponse =
    client.get(COIN_GECKO_API_BASE_URL) {
      url {
        appendPathSegments("coins", id, "market_chart")
        parameters.append("vs_currency", vsFiatCurrency.queryParam)
        parameters.append("days", days.queryParam)
        parameters.append("precision", "full")
      }
    }
}
