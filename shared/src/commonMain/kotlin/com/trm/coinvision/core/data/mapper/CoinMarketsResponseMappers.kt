package com.trm.coinvision.core.data.mapper

import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.network.model.CoinMarketsResponseItem
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

internal fun CoinMarketsResponseItem.isValid(): Boolean =
  _currentPrice != null && _id != null && _name != null && _symbol != null

internal suspend fun HttpResponse.toTokenListItems(): List<TokenListItemDTO> =
  body<List<CoinMarketsResponseItem>>().filter(CoinMarketsResponseItem::isValid)
