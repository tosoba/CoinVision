package com.trm.coinvision.core.domain.repo

import androidx.paging.PagingData
import com.trm.coinvision.core.domain.model.TokenListItemDTO
import kotlinx.coroutines.flow.Flow

internal fun interface TokenListPagingRepository {
  operator fun invoke(query: String?): Flow<PagingData<TokenListItemDTO>>
}
