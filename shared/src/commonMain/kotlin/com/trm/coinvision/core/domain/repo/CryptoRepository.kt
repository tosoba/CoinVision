package com.trm.coinvision.core.domain.repo

import androidx.annotation.IntRange
import com.trm.coinvision.core.domain.model.FiatCurrency
import io.ktor.client.statement.HttpResponse

internal interface CryptoRepository {
  suspend fun getTokens(
    vsFiatCurrency: FiatCurrency,
    page: Int = 1,
    perPage: Int = 250,
    ids: List<String>? = null,
    order: String = "market_cap_desc",
    sparkline: Boolean = false,
    locale: String = "en",
    @IntRange(from = 0L, to = 18L) precision: Short = 2
  ): HttpResponse

  suspend fun search(query: String): HttpResponse

  suspend fun getTokenById(id: String): HttpResponse
}
