package com.trm.coinvision.core.data.mapper

import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.network.model.CoinMarketsResponseItem
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

internal fun CoinMarketsResponseItem.toDomain(): CoinMarketsItem =
  CoinMarketsItem(
    currentPrice = requireNotNull(currentPrice),
    high24h = high24h,
    id = requireNotNull(id),
    low24h = low24h,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    name = requireNotNull(name),
    priceChangePercentage24h = priceChangePercentage24h,
    symbol = requireNotNull(symbol)
  )

internal fun CoinMarketsResponseItem.isValid(): Boolean =
  currentPrice != null && id != null && name != null && symbol != null

internal suspend fun HttpResponse.coinMarketsBody(): List<CoinMarketsItem> =
  body<List<CoinMarketsResponseItem>>()
    .filter(CoinMarketsResponseItem::isValid)
    .map(CoinMarketsResponseItem::toDomain)
