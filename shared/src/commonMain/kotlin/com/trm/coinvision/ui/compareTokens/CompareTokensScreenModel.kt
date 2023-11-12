package com.trm.coinvision.ui.compareTokens

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class CompareTokensScreenModel(
  selectedTokenRepository: SelectedTokenRepository,
) : ScreenModel {
  val selectedToken =
    selectedTokenRepository
      .getSelectedTokenIdFlow()
      .transformLatest {
        emit(Loadable.InProgress)
        emit(Loadable.Completed(selectedTokenRepository.getTokenById(it)))
      }
      .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = Loadable.InProgress
      )
}
