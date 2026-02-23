package com.trm.coinvision

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.navigator.Navigator
import com.trm.coinvision.core.common.util.LocalHeightSizeClass
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.MainScreen
import com.trm.coinvision.ui.common.coinVisionShimmerTheme
import com.trm.coinvision.ui.common.kamelConfig
import com.valentinilk.shimmer.LocalShimmerTheme
import io.kamel.image.config.LocalKamelConfig

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun App() {
  val windowSizeClass = calculateWindowSizeClass()
  CompositionLocalProvider(
    LocalWidthSizeClass provides windowSizeClass.widthSizeClass,
    LocalHeightSizeClass provides windowSizeClass.heightSizeClass,
    LocalShimmerTheme provides coinVisionShimmerTheme,
    LocalKamelConfig provides kamelConfig,
  ) {
    MaterialTheme { Navigator(MainScreen) }
  }
}
