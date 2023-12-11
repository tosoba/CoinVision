package com.trm.coinvision.ui.tokensList

import com.trm.coinvision.core.common.util.ext.format
import com.trm.coinvision.core.common.util.ext.formatPrice
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.model.WithData
import com.trm.coinvision.core.domain.model.WithoutData

internal class TokenPotentialComparisonMapper(
  private val mainToken: Loadable<TokenDTO>,
) : (TokenListItemDTO) -> TokenPotentialComparison {
  override operator fun invoke(referenceToken: TokenListItemDTO): TokenPotentialComparison =
    when (mainToken) {
      is WithData -> {
        val mainTokenMarketCap = mainToken.data.marketData?.marketCap?.usd
        val mainTokenPrice = mainToken.data.marketData?.currentPrice?.usd
        val referenceTokenMarketCap = referenceToken.marketCap
        TokenPotentialComparison(
          referenceToken = referenceToken,
          potential =
            if (
              mainTokenMarketCap == null ||
                mainTokenPrice == null ||
                referenceTokenMarketCap == null
            ) {
              null
            } else {
              TokenPotential(
                token = mainToken.data,
                potentialPriceFormatted =
                  (referenceTokenMarketCap / mainTokenMarketCap * mainTokenPrice).formatPrice(),
                potentialUpsideFormatted =
                  when {
                    mainToken.data.id == referenceToken.id -> null
                    referenceTokenMarketCap / mainTokenMarketCap > 10.0 ->
                      "${(referenceTokenMarketCap / mainTokenMarketCap).format(2)}x"
                    referenceTokenMarketCap / mainTokenMarketCap > 1.0 -> {
                      "+${((referenceTokenMarketCap / mainTokenMarketCap - 1.0) * 100.0).format(2)}%"
                    }
                    else -> {
                      "-${(100.0 - ((referenceTokenMarketCap / mainTokenMarketCap) * 100.0)).format(2)}%"
                    }
                  }
              )
            }
        )
      }
      is WithoutData -> {
        TokenPotentialComparison(referenceToken = referenceToken, potential = null)
      }
    }
}
