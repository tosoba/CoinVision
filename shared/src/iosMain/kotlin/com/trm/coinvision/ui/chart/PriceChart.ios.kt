package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@Composable
internal actual fun PriceChart(modifier: Modifier, points: List<PriceChartPoint>) {
  PriceChartUIView(modifier, points)
}

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun PriceChartUIView(modifier: Modifier, points: List<PriceChartPoint>) {
  val factory = LocalSharedFactory.current
  val viewFactory = remember { factory.createPriceChartUIView(points) }
  val delegate = remember(viewFactory) { viewFactory.second }
  val view = remember(viewFactory) { viewFactory.first }

  remember(points) { delegate.updatePoints(points) }
  UIKitView(modifier = modifier, factory = { view })
}

actual interface ComposeSharedFactory {
  fun createPriceChartUIView(points: List<PriceChartPoint>): Pair<UIView, PriceChartUIViewDelegate>
}

interface PriceChartUIViewDelegate {
  fun updatePoints(points: List<PriceChartPoint>)
}
