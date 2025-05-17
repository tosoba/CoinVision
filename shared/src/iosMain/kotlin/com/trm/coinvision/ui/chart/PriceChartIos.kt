package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PriceChart(modifier: Modifier, points: List<PriceChartPoint>) {
  PriceChartUIView(modifier, points)
}
