package com.trm.coinvision.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.trm.coinvision.core.data.source.TokenListNetworkPagingSource
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import com.trm.coinvision.core.domain.repo.TokenListPagingRepository
import com.trm.coinvision.core.network.client.CoinGeckoApiClient

internal fun tokenListPagingRepository(
  client: CoinGeckoApiClient,
  pagingSource: (String?) -> PagingSource<Int, TokenListItemDTO> = { query ->
    TokenListNetworkPagingSource(client, query)
  },
) = TokenListPagingRepository { query ->
  Pager(
      PagingConfig(
        pageSize = DEFAULT_PAGE_SIZE,
        initialLoadSize = DEFAULT_PAGE_SIZE,
        prefetchDistance = DEFAULT_PAGE_SIZE / 5,
      )
    ) {
      pagingSource(query)
    }
    .flow
}

private const val DEFAULT_PAGE_SIZE = 250
