package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

@Composable
internal expect fun PriceChart(modifier: Modifier = Modifier, points: List<PriceChartPoint>)

@Composable
internal expect fun PriceChartUIView(modifier: Modifier = Modifier, points: List<PriceChartPoint>)

typealias SharedFactory = ComposeSharedFactory

expect interface ComposeSharedFactory

val LocalSharedFactory: ProvidableCompositionLocal<ComposeSharedFactory> =
  compositionLocalOf(defaultFactory = { error("""You have to provide LocalSharedFactory""") })
