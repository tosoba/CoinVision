package com.trm.coinvision.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.PagingState
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import com.trm.coinvision.core.data.mapper.coinMarketsBody
import com.trm.coinvision.core.domain.model.CoinMarketsItem
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.repo.CryptoRepository
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow

internal class GetCoinMarketsPagingUseCase(private val repository: CryptoRepository) {
  operator fun invoke(query: String? = null): Flow<PagingData<CoinMarketsItem>> =
    Pager(
        PagingConfig(
          pageSize = DEFAULT_PAGE_SIZE,
          initialLoadSize = DEFAULT_PAGE_SIZE,
          prefetchDistance = DEFAULT_PAGE_SIZE / 5
        )
      ) {
        CoinMarketsPagingSource(query)
      }
      .flow

  private inner class CoinMarketsPagingSource(
    private val query: String?,
  ) : PagingSource<Int, CoinMarketsItem>() {
    override fun getRefreshKey(state: PagingState<Int, CoinMarketsItem>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinMarketsItem> {
      // TODO: use search endpoint for fetching token ids if query not blank
      val page = params.key ?: FIRST_PAGE
      return try {
        val response =
          repository.getCoinMarkets(
            vsFiatCurrency = FiatCurrency.USD,
            page = page,
            perPage = params.loadSize
          )
        if (response.status.isSuccess()) {
          val coinMarkets = response.coinMarketsBody()
          PagingSourceLoadResultPage(
            data = coinMarkets,
            prevKey = (page - 1).takeIf { it >= FIRST_PAGE },
            nextKey = (page + 1).takeIf { coinMarkets.isNotEmpty() }
          )
        } else {
          PagingSourceLoadResultError(Exception("Received a ${response.status}."))
        }
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
