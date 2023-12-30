package com.trm.coinvision.core.domain.model

import com.trm.coinvision.core.common.model.Serializable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Loadable<out T : Any> : Serializable {
  val copyWithLoadingInProgress: Loadable<T>
    get() = LoadingFirst

  val copyWithClearedError: Loadable<T>
    get() = Empty

  fun copyWithError(error: Throwable?): Loadable<T> = FailedFirst(error)

  fun <R : Any> map(block: (T) -> R): Loadable<R>

  fun <R : Any> mapNullable(orElse: (T) -> Loadable<R> = { Empty }, block: (T) -> R?): Loadable<R>
}

@OptIn(ExperimentalContracts::class)
inline fun <reified E> Loadable<*>.isFailedWith(): Boolean {
  contract { returns(true) implies (this@isFailedWith is Failed) }
  return (this as? Failed)?.throwable is E
}

sealed interface WithData<T : Any> : Loadable<T> {
  val data: T
}

sealed interface WithoutData : Loadable<Nothing>

data object Empty : WithoutData {
  override fun <R : Any> map(block: (Nothing) -> R): Loadable<R> = this

  override fun <R : Any> mapNullable(
    orElse: (Nothing) -> Loadable<R>,
    block: (Nothing) -> R?
  ): Loadable<R> = this
}

sealed interface Loading

data object LoadingFirst : WithoutData, Loading {
  override fun <R : Any> map(block: (Nothing) -> R): Loadable<R> = this

  override fun <R : Any> mapNullable(
    orElse: (Nothing) -> Loadable<R>,
    block: (Nothing) -> R?
  ): Loadable<R> = this
}

data class LoadingNext<T : Any>(override val data: T) : WithData<T>, Loading {
  override val copyWithLoadingInProgress: Loadable<T>
    get() = this

  override val copyWithClearedError: Loadable<T>
    get() = this

  override fun copyWithError(error: Throwable?): FailedNext<T> = FailedNext(data, error)

  override fun <R : Any> map(block: (T) -> R): LoadingNext<R> = LoadingNext(block(data))

  override fun <R : Any> mapNullable(orElse: (T) -> Loadable<R>, block: (T) -> R?): Loadable<R> =
    block(data)?.let(::LoadingNext) ?: Empty
}

sealed interface Failed {
  val throwable: Throwable?
}

data class FailedFirst(override val throwable: Throwable?) : WithoutData, Failed {
  override val copyWithLoadingInProgress: LoadingFirst
    get() = LoadingFirst

  override fun <R : Any> map(block: (Nothing) -> R): Loadable<R> = this

  override fun <R : Any> mapNullable(
    orElse: (Nothing) -> Loadable<R>,
    block: (Nothing) -> R?
  ): Loadable<R> = this
}

data class FailedNext<T : Any>(
  override val data: T,
  override val throwable: Throwable?,
) : WithData<T>, Failed {
  override val copyWithClearedError: Ready<T>
    get() = Ready(data)

  override val copyWithLoadingInProgress: Loadable<T>
    get() = LoadingNext(data)

  override fun copyWithError(error: Throwable?): FailedNext<T> = FailedNext(data, error)

  override fun <R : Any> map(block: (T) -> R): FailedNext<R> = FailedNext(block(data), throwable)

  override fun <R : Any> mapNullable(orElse: (T) -> Loadable<R>, block: (T) -> R?): Loadable<R> =
    block(data)?.let { FailedNext(it, throwable) } ?: Empty
}

data class Ready<T : Any>(override val data: T) : WithData<T> {
  override val copyWithLoadingInProgress: LoadingNext<T>
    get() = LoadingNext(data)

  override val copyWithClearedError: Loadable<T>
    get() = this

  override fun copyWithError(error: Throwable?): FailedNext<T> = FailedNext(data, error)

  override fun <R : Any> map(block: (T) -> R): WithData<R> = Ready(block(data))

  override fun <R : Any> mapNullable(orElse: (T) -> Loadable<R>, block: (T) -> R?): Loadable<R> =
    block(data)?.let(::Ready) ?: Empty
}

inline fun <reified T : Any> T?.asLoadable(): Loadable<T> = if (this == null) Empty else Ready(this)

inline fun <reified T : Collection<*>> T?.asLoadable(): Loadable<T> =
  if (isNullOrEmpty()) Empty else Ready(this)

fun <T : Any> Loadable<T>.dataOrElse(fallback: T): T = if (this is WithData) data else fallback

fun <T : Any> Loadable<T>.dataOrNull(): T? = if (this is WithData) data else null
