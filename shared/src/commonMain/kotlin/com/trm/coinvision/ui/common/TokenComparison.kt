package com.trm.coinvision.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.trm.coinvision.core.common.util.ext.decimalFormat
import com.trm.coinvision.core.domain.model.TokenDTO

@Composable
internal fun TokenComparison(
  modifier: Modifier = Modifier,
  mainToken: TokenDTO,
  referenceToken: TokenDTO,
) {
  val mainTokenSymbol = remember(mainToken) { mainToken.symbol }
  val referenceTokenSymbol = remember(referenceToken) { referenceToken.symbol }
  val referenceTokenPotentialPrice =
    remember(mainToken, referenceToken) {
      val mainTokenMarketCap = mainToken.marketData?.marketCap?.usd
      val mainTokenPrice = mainToken.marketData?.currentPrice?.usd
      val referenceTokenMarketCap = referenceToken.marketData?.marketCap?.usd
      if (mainTokenMarketCap == null || mainTokenPrice == null || referenceTokenMarketCap == null) {
        null
      } else {
        (referenceTokenMarketCap / mainTokenMarketCap * mainTokenPrice).decimalFormat()
      }
    }

  Crossfade(
    modifier = modifier,
    targetState =
      referenceTokenPotentialPrice != null &&
        mainTokenSymbol != null &&
        referenceTokenSymbol != null
  ) { isTokensDataComplete ->
    if (isTokensDataComplete) {
      val comparisonText = buildAnnotatedString {
        append("If ")
        appendInlineContent(id = "mainTokenImage")
        append(" ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(mainToken.symbol.orEmpty().uppercase())
        pop()
        append(" had the market cap of ")
        appendInlineContent(id = "referenceTokenImage")
        append(" ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(referenceToken.symbol.orEmpty().uppercase())
        pop()
        append(", ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append("1 ")
        append(mainToken.symbol.orEmpty().uppercase())
        pop()
        append(" would be worth ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(referenceTokenPotentialPrice)
        append("$")
        pop()
        append(".")
      }
      val inlineContentMap =
        mapOf(
          "mainTokenImage" to
            InlineTextContent(
              Placeholder(
                width = 32.sp,
                height = 32.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
              )
            ) {
              TokenImageOrSymbol(
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                image = mainToken.image?.small,
                symbol = mainTokenSymbol.orEmpty(),
                name = mainToken.name.orEmpty()
              )
            },
          "referenceTokenImage" to
            InlineTextContent(
              Placeholder(
                width = 32.sp,
                height = 32.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
              )
            ) {
              TokenImageOrSymbol(
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                image = referenceToken.image?.small,
                symbol = referenceTokenSymbol.orEmpty(),
                name = referenceToken.name.orEmpty()
              )
            }
        )
      Box(modifier = Modifier.fillMaxSize()) {
        Text(
          modifier = Modifier.align(Alignment.Center),
          text = comparisonText,
          inlineContent = inlineContentMap,
          textAlign = TextAlign.Center,
          lineHeight = 32.sp,
          fontSize = 24.sp,
          overflow = TextOverflow.Ellipsis
        )
      }
    } else {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(
          modifier = Modifier.align(Alignment.Center),
          text =
            "Data retrieved for at least one of selected tokens is incomplete - choose a different token pair.", // TODO: StringRes
          textAlign = TextAlign.Center,
          fontSize = 24.sp,
          lineHeight = 32.sp,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
