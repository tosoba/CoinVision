package com.trm.coinvision.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trm.coinvision.core.domain.model.TokenDTO

@Composable
internal fun SelectedTokenData(
  modifier: Modifier = Modifier,
  mainToken: TokenDTO,
  referenceToken: TokenDTO,
) {
  // TODO: layout
  Box(modifier = modifier) {
    Text(
      modifier = Modifier.align(Alignment.Center),
      text = "${referenceToken.name.orEmpty()} ${referenceToken.marketData?.priceChange24h}"
    )
  }
}
