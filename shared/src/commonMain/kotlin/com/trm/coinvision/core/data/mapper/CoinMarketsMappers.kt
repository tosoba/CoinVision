package com.trm.coinvision.core.data.mapper

import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.network.model.CoinMarketsResponseItem
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

fun CoinMarketsResponseItem.toDomain(): CoinMarketsItem =
  CoinMarketsItem(
    requireNotNull(currentPrice),
    high24h,
    requireNotNull(id),
    low24h,
    marketCap,
    marketCapRank,
    requireNotNull(name),
    priceChangePercentage24h,
    requireNotNull(symbol)
  )

fun CoinMarketsResponseItem.isValid(): Boolean =
  currentPrice != null && id != null && name != null && symbol != null

suspend fun HttpResponse.coinMarketsBody(): List<CoinMarketsItem> =
  body<List<CoinMarketsResponseItem>>()
    .filter(CoinMarketsResponseItem::isValid)
    .map(CoinMarketsResponseItem::toDomain)
