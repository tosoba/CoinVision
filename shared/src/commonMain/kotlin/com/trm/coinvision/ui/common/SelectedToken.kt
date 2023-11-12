package com.trm.coinvision.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.TokenDTO

@Composable
internal fun SelectedTokenData(modifier: Modifier = Modifier, token: Loadable<TokenDTO>) {
  Box(modifier = modifier) {
    when (token) {
      is Loadable.Completed -> {
        token.result.fold(
          onSuccess = {
            Text(
              modifier = Modifier.align(Alignment.Center),
              text = "${it.name.orEmpty()} ${it.marketData?.priceChange24h}"
            )
          },
          onFailure = {
            CoinVisionRetryColumn(modifier = Modifier.align(Alignment.Center)) {
              // TODO: retry
            }
          }
        )
      }
      Loadable.InProgress -> {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
      }
    }
  }
}
