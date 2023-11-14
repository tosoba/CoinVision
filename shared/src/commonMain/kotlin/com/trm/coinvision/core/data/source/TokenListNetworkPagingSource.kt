package com.trm.coinvision.core.data.source

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.network.client.CoinGeckoApiClient
import com.trm.coinvision.core.network.mapper.toTokenListItems
import com.trm.coinvision.core.network.model.Coin
import com.trm.coinvision.core.network.model.SearchResponse
import io.ktor.client.call.body
import io.ktor.http.isSuccess

internal class TokenListNetworkPagingSource(
  private val client: CoinGeckoApiClient,
  private val query: String?,
) : PagingSource<Int, TokenListItemDTO>() {
  override fun getRefreshKey(state: PagingState<Int, TokenListItemDTO>): Int? = null

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TokenListItemDTO> {
    val ids =
      if (query != null) {
        try {
          val ids = client.search(query).body<SearchResponse>().coins?.mapNotNull(Coin::id)
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
        client.getTokens(
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
          nextKey = (page + 1).takeIf { tokens.size == params.loadSize }
        )
      } else {
        PagingSourceLoadResultError(Exception("Received a ${response.status}."))
      }
    } catch (ex: Exception) {
      PagingSourceLoadResultError(ex)
    }
  }
}

private const val FIRST_PAGE = 1
