package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.domain.usecase.GetCoinMarketsPagingUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class TokensListScreenModel(
  getCoinMarketsPagingUseCase: GetCoinMarketsPagingUseCase,
) : ScreenModel {
  val coinMarkets: StateFlow<PagingData<CoinMarketsItem>> =
    getCoinMarketsPagingUseCase(null)
      .cachedIn(coroutineScope)
      .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5_000L), PagingData.empty())
}
