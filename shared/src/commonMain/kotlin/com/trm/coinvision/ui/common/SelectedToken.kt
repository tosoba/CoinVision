package com.trm.coinvision.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trm.coinvision.core.domain.model.Failed
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.Loading
import com.trm.coinvision.core.domain.model.Ready
import com.trm.coinvision.core.domain.model.TokenDTO

@Composable
internal fun SelectedTokenData(modifier: Modifier = Modifier, token: Loadable<TokenDTO>) {
  Box(modifier = modifier) {
    when (token) {
      is Loading -> {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
      }
      is Ready -> {
        Text(
          modifier = Modifier.align(Alignment.Center),
          text = "${token.data.name.orEmpty()} ${token.data.marketData?.priceChange24h}"
        )
      }
      is Failed -> {
        CoinVisionRetryColumn(modifier = Modifier.align(Alignment.Center)) {
          // TODO: retry
        }
      }
      else -> {}
    }
  }
}
