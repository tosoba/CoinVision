package com.trm.coinvision.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.domain.model.Empty
import com.trm.coinvision.core.domain.model.Failed
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.Loading
import com.trm.coinvision.core.domain.model.Ready

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
      text = it?.errorText() ?: LocalStringResources.current.errorOccurred,
      onRetryClick = onRetryClick
    )
  },
  emptyContent: @Composable () -> Unit = {},
  readyContent: @Composable (T) -> Unit = {}
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
