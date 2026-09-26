package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

@Composable
internal expect fun PriceChart(modifier: Modifier = Modifier, points: List<PriceChartPoint>)

expect interface ComposeSharedFactory

val LocalComposeSharedFactory: ProvidableCompositionLocal<ComposeSharedFactory> =
  compositionLocalOf(defaultFactory = { error("LocalComposeSharedFactory was not provided.") })
