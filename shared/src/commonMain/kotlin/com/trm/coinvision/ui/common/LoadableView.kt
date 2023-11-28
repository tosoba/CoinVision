package com.trm.coinvision.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
  content: @Composable (T) -> Unit = {}
) {
  Crossfade(targetState = loadable, modifier = modifier) {
    when (loadable) {
      is Loading -> {
        Box(modifier = Modifier.fillMaxSize()) {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
      }
      is Ready -> {
        content(loadable.data)
      }
      is Failed -> {
        CoinVisionRetryColumn(modifier = Modifier.fillMaxSize(), onRetryClick = onRetryClick)
      }
      Empty -> {}
    }
  }
}
