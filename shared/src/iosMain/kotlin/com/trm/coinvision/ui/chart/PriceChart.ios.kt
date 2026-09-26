package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.UIKit.UIView

@Composable
internal actual fun PriceChart(points: List<PriceChartPoint>, modifier: Modifier) {
  val factory = LocalComposeSharedFactory.current
  val (view, delegate) = remember { factory.createPriceChartUIView(points) }
  LaunchedEffect(points) { delegate.updatePoints(points) }
  UIKitView(modifier = modifier, factory = { view })
}

actual interface ComposeSharedFactory {
  fun createPriceChartUIView(points: List<PriceChartPoint>): Pair<UIView, PriceChartUIViewDelegate>
}

interface PriceChartUIViewDelegate {
  fun updatePoints(points: List<PriceChartPoint>)
}
