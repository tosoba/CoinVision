package com.trm.coinvision.ui.common

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.trm.coinvision.core.common.util.LocalHeightSizeClass
import com.trm.coinvision.core.common.util.LocalWidthSizeClass

internal val usingHorizontalTabSplit: Boolean
  @Composable
  get() =
    LocalWidthSizeClass.current != WindowWidthSizeClass.Compact &&
      LocalHeightSizeClass.current != WindowHeightSizeClass.Expanded

internal val usingNavigationBar
  @Composable
  get() =
    LocalWidthSizeClass.current == WindowWidthSizeClass.Compact ||
      LocalHeightSizeClass.current == WindowHeightSizeClass.Medium ||
      LocalHeightSizeClass.current == WindowHeightSizeClass.Expanded
