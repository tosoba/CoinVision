package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.domain.usecase.GetCoinMarketsPagingUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
internal class TokensListScreenModel(
  private val getCoinMarketsPagingUseCase: GetCoinMarketsPagingUseCase
) : ScreenModel {
  private val queryFlow = MutableSharedFlow<String?>()

  val coinMarkets: StateFlow<PagingData<CoinMarketsItem>> =
    queryFlow
      .onStart { emit(null) }
      .distinctUntilChanged()
      .flatMapLatest { getCoinMarketsPagingUseCase(it).cachedIn(coroutineScope) }
      .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5_000L), PagingData.empty())
}
