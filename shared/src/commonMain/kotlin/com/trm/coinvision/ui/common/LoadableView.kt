package com.trm.coinvision.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.error_occurred
import com.trm.coinvision.core.common.util.ext.requireAs
import com.trm.coinvision.core.common.util.ext.safeAs
import com.trm.coinvision.core.domain.model.Empty
import com.trm.coinvision.core.domain.model.Failed
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.Loading
import com.trm.coinvision.core.domain.model.Ready
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T : Any> LoadableView(
  modifier: Modifier = Modifier,
  loadable: Loadable<T> = Empty,
  onRetryClick: () -> Unit = {},
  loadingContent: @Composable () -> Unit = {
    Box(modifier = Modifier.fillMaxSize()) {
      CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
  },
  failedContent: @Composable (Throwable?) -> Unit = {
    CoinVisionRetryColumn(
      modifier = Modifier.fillMaxSize(),
      text = it?.errorText() ?: stringResource(Res.string.error_occurred),
      onRetryClick = onRetryClick,
    )
  },
  emptyContent: @Composable () -> Unit = {},
  readyContent: @Composable (T) -> Unit = {},
) {
  Crossfade(targetState = loadable, modifier = modifier) {
    when (loadable) {
      is Loading -> loadingContent()
      is Ready -> readyContent(loadable.data)
      is Failed -> failedContent(loadable.throwable)
      Empty -> emptyContent()
    }
  }
}

@Composable
fun <T : Any, S : Any> LoadableView(
  modifier: Modifier = Modifier,
  loadable1: Loadable<T>,
  loadable2: Loadable<S>,
  onRetryClick1: () -> Unit = {},
  onRetryClick2: () -> Unit = {},
  loadingContent: @Composable () -> Unit = {
    Box(modifier = Modifier.fillMaxSize()) {
      CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
  },
  failedContent: @Composable (Throwable?, Throwable?) -> Unit = { throwable1, throwable2 ->
    val errorText1 = throwable1?.errorText()
    val errorText2 = throwable2?.errorText()
    CoinVisionRetryColumn(
      modifier = Modifier.fillMaxSize(),
      text =
        errorText1?.takeIf { errorText1 == errorText2 }
          ?: stringResource(Res.string.error_occurred),
      onRetryClick = {
        if (loadable1 is Failed) onRetryClick1()
        if (loadable2 is Failed) onRetryClick2()
      },
    )
  },
  emptyContent: @Composable () -> Unit = {},
  readyContent: @Composable (T, S) -> Unit = { _, _ -> },
) {
  Crossfade(targetState = listOf(loadable1, loadable2), modifier = modifier) { loadables ->
    when {
      loadables.any { it is Loading } -> loadingContent()
      loadables.any { it is Failed } -> {
        failedContent(
          loadables.firstOrNull()?.safeAs<Failed>()?.throwable,
          loadables.lastOrNull()?.safeAs<Failed>()?.throwable,
        )
      }
      loadables.any { it is Empty } -> emptyContent()
      loadables.all { it is Ready } -> {
        readyContent(
          loadables.first().requireAs<Ready<T>>().data,
          loadables.last().requireAs<Ready<S>>().data,
        )
      }
    }
  }
}
