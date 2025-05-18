package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.compose.swift.bridge.ExpectSwiftView
import co.touchlab.compose.swift.bridge.ViewType

@Composable
internal expect fun PriceChart(modifier: Modifier = Modifier, points: List<PriceChartPoint>)

@ExpectSwiftView(type = ViewType.UIView)
@Composable
internal expect fun PriceChartUIView(modifier: Modifier = Modifier, points: List<PriceChartPoint>)
