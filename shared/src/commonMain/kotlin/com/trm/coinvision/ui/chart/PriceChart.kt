package com.trm.coinvision.ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun PriceChart(modifier: Modifier = Modifier, points: List<PriceChartPoint>)
