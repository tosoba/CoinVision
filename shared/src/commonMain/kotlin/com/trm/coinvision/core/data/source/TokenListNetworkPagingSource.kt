package com.trm.coinvision.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.trm.coinvision.core.domain.model.FiatCurrency
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.network.client.CoinGeckoApiClient
import com.trm.coinvision.core.network.mapper.toTokenListItems
import com.trm.coinvision.core.network.model.Coin
import com.trm.coinvision.core.network.model.SearchResponse
import io.ktor.client.call.body
import kotlinx.coroutines.CancellationException

internal class TokenListNetworkPagingSource(
  private val client: CoinGeckoApiClient,
  private val query: String?,
) : PagingSource<Int, TokenListItemDTO>() {
  override fun getRefreshKey(state: PagingState<Int, TokenListItemDTO>): Int? = null

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TokenListItemDTO> {
    val page = params.key ?: FIRST_PAGE
    return try {
      val ids =
        if (query != null) {
          client.search(query).body<SearchResponse>().coins?.mapNotNull(Coin::id)
        } else {
          null
        }
      if (query != null && ids.isNullOrEmpty()) {
        return LoadResult.Page(emptyList(), null, null)
      }

      val tokens =
        client
          .getTokens(
            vsFiatCurrency = FiatCurrency.USD,
            ids = ids,
            page = page,
            perPage = params.loadSize,
          )
          .toTokenListItems()
      LoadResult.Page(
        data = tokens,
        prevKey = (page - 1).takeIf { it >= FIRST_PAGE },
        nextKey = (page + 1).takeIf { tokens.size == params.loadSize },
      )
    } catch (ex: CancellationException) {
      throw ex
    } catch (ex: Exception) {
      LoadResult.Error(ex)
    }
  }
}

private const val FIRST_PAGE = 1
