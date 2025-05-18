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
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.api_error_occurred
import coinvision.shared.generated.resources.error_occurred
import coinvision.shared.generated.resources.exceeded_rate_limit
import coinvision.shared.generated.resources.no_internet_connection
import coinvision.shared.generated.resources.retry
import com.trm.coinvision.core.domain.exception.HttpException
import io.ktor.client.plugins.ResponseException
import io.ktor.utils.io.errors.IOException
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CoinVisionRetryRow(
  modifier: Modifier = Modifier,
  text: String = stringResource(Res.string.error_occurred),
  onRetryClick: () -> Unit = {},
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    Text(text = text, maxLines = 2, overflow = TextOverflow.Ellipsis)
    Spacer(modifier = Modifier.width(5.dp))
    Button(onRetryClick) { Text(text = stringResource(Res.string.retry)) }
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
  text: String = stringResource(Res.string.error_occurred),
  onRetryClick: () -> Unit = {},
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = text, maxLines = 2, overflow = TextOverflow.Ellipsis)
    Spacer(modifier = Modifier.height(5.dp))
    Button(onRetryClick) { Text(text = stringResource(Res.string.retry)) }
  }
}

@Composable
fun Throwable.errorText(): String =
  stringResource(
    when (this) {
      is ResponseException -> {
        if (response.status.value == 429) Res.string.exceeded_rate_limit
        else Res.string.api_error_occurred
      }
      is HttpException -> {
        if (status.value == 429) Res.string.exceeded_rate_limit else Res.string.api_error_occurred
      }
      is IOException -> Res.string.no_internet_connection
      else -> Res.string.error_occurred
    }
  )
