package com.trm.coinvision.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun TokenImageOrSymbol(
  modifier: Modifier = Modifier,
  image: String?,
  symbol: String,
  name: String,
) {
  image?.let {
    KamelImage(
      modifier = modifier,
      resource = { asyncPainterResource(it) },
      contentDescription = name,
      onFailure = { TokenSymbol(symbol) },
      onLoading = { TokenSymbol(symbol = symbol, modifier = Modifier.tokenSymbolShape().shimmer()) },
    )
  } ?: run { TokenSymbol(symbol) }
}
