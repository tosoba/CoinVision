package com.trm.coinvision.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import com.valentinilk.shimmer.defaultShimmerTheme

internal val coinVisionShimmerTheme =
  defaultShimmerTheme.copy(
    animationSpec =
      infiniteRepeatable(
        animation =
          tween(
            800,
            easing = LinearEasing,
            delayMillis = 0,
          ),
        repeatMode = RepeatMode.Restart,
      )
  )
