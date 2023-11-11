package com.trm.coinvision.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.PagingState
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import com.trm.coinvision.core.data.mapper.toTokenListItems
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.repo.CryptoRepository
import com.trm.coinvision.core.network.model.Coin
import com.trm.coinvision.core.network.model.SearchResponse
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow

internal class GetTokensPagingUseCase(private val repository: CryptoRepository) {
  operator fun invoke(query: String? = null): Flow<PagingData<TokenListItemDTO>> =
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
  ) : PagingSource<Int, TokenListItemDTO>() {
    override fun getRefreshKey(state: PagingState<Int, TokenListItemDTO>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TokenListItemDTO> {
      val ids =
        if (query != null) {
          try {
            val ids = repository.search(query).body<SearchResponse>().coins?.mapNotNull(Coin::id)
            if (ids.isNullOrEmpty()) {
              return PagingSourceLoadResultPage(emptyList(), null, null)
            }
            ids
          } catch (ex: Exception) {
            return PagingSourceLoadResultError(ex)
          }
        } else {
          null
        }

      val page = params.key ?: FIRST_PAGE
      return try {
        val response =
          repository.getTokens(
            vsFiatCurrency = FiatCurrency.USD,
            ids = ids,
            page = page,
            perPage = params.loadSize
          )
        if (response.status.isSuccess()) {
          val tokens = response.toTokenListItems()
          PagingSourceLoadResultPage(
            data = tokens,
            prevKey = (page - 1).takeIf { it >= FIRST_PAGE },
            nextKey = (page + 1).takeIf { tokens.isNotEmpty() }
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
