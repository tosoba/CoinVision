package com.trm.coinvision.ui

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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainScreenModel(
  private val getCoinMarketsPagingUseCase: GetCoinMarketsPagingUseCase
) : ScreenModel {
  val queryFlow = MutableSharedFlow<String>()

  val coinMarkets: StateFlow<PagingData<CoinMarketsItem>> =
    queryFlow
      .onStart { emit("") }
      .map { it.takeIf(String::isNotBlank) }
      .flatMapLatest { getCoinMarketsPagingUseCase(it).cachedIn(coroutineScope) }
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PagingData.empty()
      )

  fun onQueryChange(query: String) {
    coroutineScope.launch { queryFlow.emit(query) }
  }
}
