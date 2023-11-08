package com.trm.coinvision.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TokenSymbol(symbol: String = "", modifier: Modifier = Modifier.tokenSymbolShape()) {
  Box(modifier = modifier) {
    Text(
      modifier = Modifier.align(Alignment.Center).basicMarquee(),
      text = symbol.toUpperCase(LocaleList.current),
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onPrimary,
      maxLines = 1,
    )
  }
}

@Composable
internal fun Modifier.tokenSymbolShape(): Modifier =
  then(
    Modifier.background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(5.dp))
      .size(40.dp)
  )
