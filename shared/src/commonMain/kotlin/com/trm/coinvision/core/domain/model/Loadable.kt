package com.trm.coinvision.core.domain.model

sealed interface Loadable<out T> {
  data object InProgress : Loadable<Nothing>

  data class Completed<T>(val result: Result<T>) : Loadable<T>
}
