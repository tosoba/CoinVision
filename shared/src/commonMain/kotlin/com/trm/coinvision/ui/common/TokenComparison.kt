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
import coinvision.shared.generated.resources.Res
import coinvision.shared.generated.resources.had_market_cap_of
import coinvision.shared.generated.resources.if_label
import coinvision.shared.generated.resources.token_comparison_data_incomplete
import coinvision.shared.generated.resources.would_be_worth
import com.trm.coinvision.core.common.util.ext.decimalFormat
import com.trm.coinvision.core.domain.model.TokenDTO
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
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
        referenceTokenSymbol != null,
  ) { isTokensDataComplete ->
    if (isTokensDataComplete) {
      val comparisonText = buildAnnotatedString {
        append(stringResource(Res.string.if_label))
        append(" ")
        appendInlineContent(id = MAIN_TOKEN_IMAGE_ID)
        append(" ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(mainToken.symbol.orEmpty().uppercase())
        pop()
        append(" ")
        append(stringResource(Res.string.had_market_cap_of))
        append(" ")
        appendInlineContent(id = REFERENCE_TOKEN_IMAGE_ID)
        append(" ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(referenceToken.symbol.orEmpty().uppercase())
        pop()
        append(", ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append("1 ")
        append(mainToken.symbol.orEmpty().uppercase())
        pop()
        append(" ")
        append(stringResource(Res.string.would_be_worth))
        append(" ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(referenceTokenPotentialPrice)
        append("$")
        pop()
        append(".")
      }
      val inlineContentMap =
        remember(mainToken, referenceToken) {
          mapOf(
            MAIN_TOKEN_IMAGE_ID to
              InlineTextContent(
                Placeholder(
                  width = 32.sp,
                  height = 32.sp,
                  placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                )
              ) {
                TokenImageOrSymbol(
                  modifier = Modifier.fillMaxSize().clip(CircleShape),
                  image = mainToken.image?.small,
                  symbol = mainTokenSymbol.orEmpty(),
                  name = mainToken.name.orEmpty(),
                )
              },
            REFERENCE_TOKEN_IMAGE_ID to
              InlineTextContent(
                Placeholder(
                  width = 32.sp,
                  height = 32.sp,
                  placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                )
              ) {
                TokenImageOrSymbol(
                  modifier = Modifier.fillMaxSize().clip(CircleShape),
                  image = referenceToken.image?.small,
                  symbol = referenceTokenSymbol.orEmpty(),
                  name = referenceToken.name.orEmpty(),
                )
              },
          )
        }
      Box(modifier = Modifier.fillMaxSize()) {
        Text(
          modifier = Modifier.align(Alignment.Center),
          text = comparisonText,
          inlineContent = inlineContentMap,
          textAlign = TextAlign.Center,
          lineHeight = 32.sp,
          fontSize = 24.sp,
          overflow = TextOverflow.Ellipsis,
        )
      }
    } else {
      Box(modifier = Modifier.fillMaxSize()) {
        Text(
          modifier = Modifier.align(Alignment.Center),
          text = stringResource(Res.string.token_comparison_data_incomplete),
          textAlign = TextAlign.Center,
          fontSize = 24.sp,
          lineHeight = 32.sp,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

private const val MAIN_TOKEN_IMAGE_ID = "mainTokenImage"
private const val REFERENCE_TOKEN_IMAGE_ID = "referenceTokenImage"
