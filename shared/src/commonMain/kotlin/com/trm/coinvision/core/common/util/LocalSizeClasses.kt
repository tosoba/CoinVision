package com.trm.coinvision.core.common.util

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.compositionLocalOf

val LocalWidthSizeClass = compositionLocalOf { WindowWidthSizeClass.Compact }
val LocalHeightSizeClass = compositionLocalOf { WindowHeightSizeClass.Compact }
