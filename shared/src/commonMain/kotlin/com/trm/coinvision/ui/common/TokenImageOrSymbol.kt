package com.trm.coinvision.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

@Composable
fun TokenImageOrSymbol(
    modifier: Modifier = Modifier,
    image: String?,
    symbol: String,
    name: String
) {
  image?.let {
      KamelImage(
          modifier = modifier,
          resource = asyncPainterResource(data = Url(it)),
          contentDescription = name,
          onFailure = { TokenSymbol(symbol = symbol) },
          onLoading = {
              TokenSymbol(
                  symbol = symbol,
                  modifier = Modifier.tokenSymbolShape().shimmer()
              )
          }
      )
  } ?: run { TokenSymbol(symbol = symbol) }
}