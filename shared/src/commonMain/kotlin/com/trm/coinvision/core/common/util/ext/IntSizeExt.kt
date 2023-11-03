package com.trm.coinvision.core.common.util.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

@Composable fun IntSize.toDp() = with(LocalDensity.current) { DpSize(width.toDp(), height.toDp()) }
