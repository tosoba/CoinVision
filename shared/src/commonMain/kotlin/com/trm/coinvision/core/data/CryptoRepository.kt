package com.trm.coinvision.core.data

import coingecko.CoinGeckoClient
import coingecko.models.coins.CoinMarketsList

class CryptoRepository(private val coinGeckoClient: CoinGeckoClient) {
  suspend fun getCoinMarkets(vsFiatCurrency: String): CoinMarketsList =
    coinGeckoClient.getCoinMarkets(vsFiatCurrency)
}
