package com.trm.coinvision.core.data

import com.trm.coinvision.core.data.repo.selectedTokenRepository
import com.trm.coinvision.core.data.repo.tokenListPagingRepository
import com.trm.coinvision.core.data.source.SelectedTokenResultInMemoryDataSource
import org.koin.dsl.module

internal val dataModule = module {
  factory { tokenListPagingRepository(get()) }

  factory { selectedTokenRepository(get(), get(), get()) }
  single { SelectedTokenResultInMemoryDataSource() }
}
