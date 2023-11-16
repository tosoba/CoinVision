package com.trm.coinvision

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import com.trm.coinvision.core.common.util.LocalHeightSizeClass
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.core.common.util.PlatformLocaleChangedObserverEffect
import com.trm.coinvision.core.common.util.stringResources
import com.trm.coinvision.ui.MainScreen
import com.trm.coinvision.ui.common.coinVisionShimmerTheme
import com.valentinilk.shimmer.LocalShimmerTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun App() {
  var stringResources by remember { mutableStateOf(stringResources()) }
  PlatformLocaleChangedObserverEffect { stringResources = stringResources(it) }

  val windowSizeClass = calculateWindowSizeClass()

  CompositionLocalProvider(
    LocalStringResources provides stringResources,
    LocalWidthSizeClass provides windowSizeClass.widthSizeClass,
    LocalHeightSizeClass provides windowSizeClass.heightSizeClass,
    LocalShimmerTheme provides coinVisionShimmerTheme
  ) {
    MaterialTheme { Navigator(MainScreen) }
  }
}
