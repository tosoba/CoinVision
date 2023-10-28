package com.trm.coinvision.core.domain.usecase

import androidx.paging.PagingState
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.repo.CryptoRepository

class CoinMarketsPager(private val repository: CryptoRepository) {
  private val pager: Pager<Int, CoinMarketsItem> =
    Pager(PagingConfig(pageSize = DEFAULT_PAGE_SIZE, initialLoadSize = DEFAULT_PAGE_SIZE)) {
      CoinMarketsPagingSource() // TODO: pass query if provided
    }

  inner class CoinMarketsPagingSource(
    private val query: String? = null,
  ) : PagingSource<Int, CoinMarketsItem>() {
    override fun getRefreshKey(state: PagingState<Int, CoinMarketsItem>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinMarketsItem> {
      val page = params.key ?: 1
      return try {
        PagingSourceLoadResultPage(
          data = repository.getCoinMarkets(FiatCurrency.USD, page, params.loadSize),
          prevKey = (page - 1).takeIf { it > FIRST_PAGE },
          nextKey = page + 1
        )
      } catch (ex: Exception) {
        PagingSourceLoadResultError(ex)
      }
    }
  }

  companion object {
    private const val FIRST_PAGE = 1
    private const val DEFAULT_PAGE_SIZE = 250
  }
}
