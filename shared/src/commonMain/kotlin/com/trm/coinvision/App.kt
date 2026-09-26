package com.trm.coinvision

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import com.trm.coinvision.core.common.util.LocalHeightSizeClass
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.MainScreen
import com.trm.coinvision.ui.common.coinVisionShimmerTheme
import com.trm.coinvision.ui.common.kamelConfig
import com.trm.coinvision.ui.compareTokens.CompareTokensRoute
import com.trm.coinvision.ui.tokensList.TokensListRoute
import com.valentinilk.shimmer.LocalShimmerTheme
import io.kamel.image.config.LocalKamelConfig

@Composable
fun App() {
  CoinVisionTheme { MainScreen() }
}

@Composable
fun CompareTokensTab() {
  CoinVisionTheme {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) {
      CompareTokensRoute(modifier = Modifier.fillMaxSize().padding(it))
    }
  }
}

@Composable
fun TokensListTab() {
  CoinVisionTheme {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) {
      TokensListRoute(
        modifier =
          Modifier.fillMaxSize()
            .padding(
              top = it.calculateTopPadding(),
              start = it.calculateStartPadding(LocalLayoutDirection.current),
              end = it.calculateEndPadding(LocalLayoutDirection.current),
            )
      )
    }
  }
}

@Composable
private fun CoinVisionTheme(content: @Composable () -> Unit) {
  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  val windowSizeClass = calculateWindowSizeClass()

  CompositionLocalProvider(
    LocalWidthSizeClass provides windowSizeClass.widthSizeClass,
    LocalHeightSizeClass provides windowSizeClass.heightSizeClass,
    LocalShimmerTheme provides coinVisionShimmerTheme,
    LocalKamelConfig provides kamelConfig,
  ) {
    MaterialTheme(
      colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
      content()
    }
  }
}
