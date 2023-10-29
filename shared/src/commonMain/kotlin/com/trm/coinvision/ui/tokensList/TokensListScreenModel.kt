package com.trm.coinvision.ui.tokensList

import androidx.paging.PagingData
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.domain.usecase.GetCoinMarketsPagingUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class TokensListScreenModel(
  private val getCoinMarketsPagingUseCase: GetCoinMarketsPagingUseCase
) : ScreenModel {
  private val queryFlow = MutableSharedFlow<String?>()

  val coinMarkets: Flow<PagingData<CoinMarketsItem>> =
    queryFlow
      .onStart { emit(null) }
      .flatMapLatest { getCoinMarketsPagingUseCase.invoke(it) }
      .cachedIn(coroutineScope)

  fun onSearchQuery(query: String?) {
    coroutineScope.launch { queryFlow.emit(query) }
  }
}
