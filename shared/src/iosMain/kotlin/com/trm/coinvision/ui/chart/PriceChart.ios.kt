package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun PriceChart(modifier: Modifier, points: List<PriceChartPoint>) {
  val factory = LocalSharedFactory.current
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
