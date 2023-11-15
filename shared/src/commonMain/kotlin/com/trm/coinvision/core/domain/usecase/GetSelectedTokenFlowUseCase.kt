package com.trm.coinvision.core.domain.usecase

import com.trm.coinvision.core.domain.model.FailedFirst
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.LoadingFirst
import com.trm.coinvision.core.domain.model.Ready
import com.trm.coinvision.core.domain.model.TokenDTO
import com.trm.coinvision.core.domain.repo.SelectedTokenRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

internal class GetSelectedTokenFlowUseCase(private val repository: SelectedTokenRepository) {
  @OptIn(ExperimentalCoroutinesApi::class)
  operator fun invoke(): Flow<Loadable<TokenDTO>> =
    repository.getSelectedTokenIdFlow().transformLatest {
      emit(LoadingFirst)
      try {
        emit(Ready(repository.getTokenById(it)))
      } catch (ex: CancellationException) {
        throw ex
      } catch (ex: Exception) {
        emit(FailedFirst(ex))
      }
    }
}
