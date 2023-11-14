package com.trm.coinvision.core.domain.usecase

import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.transformLatest

internal class GetSelectedTokenFlowUseCase(private val repository: SelectedTokenRepository) {
  @OptIn(ExperimentalCoroutinesApi::class)
  operator fun invoke() =
    repository.getSelectedTokenIdFlow().transformLatest {
      emit(Loadable.InProgress)
      emit(Loadable.Completed(repository.getTokenById(it)))
    }
}
