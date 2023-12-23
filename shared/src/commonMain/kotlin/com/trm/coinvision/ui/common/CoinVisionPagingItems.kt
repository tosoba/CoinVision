package com.trm.coinvision.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.domain.exception.HttpException
import io.ktor.utils.io.errors.IOException

@Composable
internal fun CoinVisionRetryRow(
  modifier: Modifier = Modifier,
  text: String = LocalStringResources.current.errorOccurred,
  onRetryClick: () -> Unit = {}
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    Text(text = text, maxLines = 2, overflow = TextOverflow.Ellipsis)
    Spacer(modifier = Modifier.width(5.dp))
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
internal fun CoinVisionRetryColumn(
  modifier: Modifier = Modifier,
  text: String = LocalStringResources.current.errorOccurred,
  onRetryClick: () -> Unit = {}
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = text, maxLines = 2, overflow = TextOverflow.Ellipsis)
    Spacer(modifier = Modifier.height(5.dp))
    Button(onRetryClick) { Text(text = LocalStringResources.current.retry) }
  }
}

@Composable
fun Throwable.errorText(): String =
  when (this) {
    is HttpException -> {
      if (status.value == 429) "Exceeded request rate limit." else "Backend error occurred."
    }
    is IOException -> "No internet connection."
    else -> LocalStringResources.current.errorOccurred
  }
