package com.trm.coinvision.core.domain.repo

import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.domain.model.FiatCurrency

interface CryptoRepository {
  suspend fun getCoinMarkets(vsFiatCurrency: FiatCurrency): List<CoinMarketsItem>
}
