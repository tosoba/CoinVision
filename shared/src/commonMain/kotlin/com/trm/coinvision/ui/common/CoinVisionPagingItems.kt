package com.trm.coinvision.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trm.coinvision.core.common.util.LocalStringResources

@Composable
internal fun CoinVisionRetryRow(modifier: Modifier = Modifier, onRetryClick: () -> Unit = {}) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    Text(text = LocalStringResources.current.errorOccurred)
    Button(onRetryClick) { Text(text = LocalStringResources.current.retry) }
  }
}

@Composable
internal fun CoinVisionProgressIndicator(modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
  }
}

@Composable
internal fun CoinVisionRetryColumn(modifier: Modifier = Modifier, onRetryClick: () -> Unit = {}) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = LocalStringResources.current.errorOccurred)
    Button(onRetryClick) { Text(text = LocalStringResources.current.retry) }
  }
}
