package com.trm.coinvision.core.network.util

import coingecko.CoinGeckoClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

fun testCoinGeckoClient(): CoinGeckoClient =
  CoinGeckoClient(HttpClient { install(Logging) { level = LogLevel.BODY } })
