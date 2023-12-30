package com.trm.coinvision.ui.chart

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trm.coinvision.core.common.util.ext.decimalFormat
import com.trm.coinvision.core.domain.model.Empty
import com.trm.coinvision.core.domain.model.Loadable
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod
import com.trm.coinvision.core.domain.model.TokenMarketDataDTO
import com.trm.coinvision.ui.common.LoadableView
import com.trm.coinvision.ui.common.SegmentedButton

@Composable
internal fun PriceChartHeader(
  modifier: Modifier = Modifier,
  daysPeriodScrollState: ScrollState = rememberScrollState(),
  marketData: Loadable<TokenMarketDataDTO> = Empty,
  chartPeriod: MarketChartDaysPeriod = MarketChartDaysPeriod.DAY,
  onChartPeriodClick: (MarketChartDaysPeriod) -> Unit = {}
) {
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    SegmentedButton(
      modifier = Modifier.weight(1f).horizontalScroll(daysPeriodScrollState),
      items = MarketChartDaysPeriod.entries.toList(),
      selectedItem = chartPeriod,
      label = MarketChartDaysPeriod::label,
      onItemClick = onChartPeriodClick
    )

    Spacer(modifier = Modifier.width(5.dp))

    LoadableView(
      loadable = marketData,
      loadingContent = {},
      failedContent = {},
    ) { marketData ->
      val price =
        remember(marketData) {
          marketData.currentPrice?.usd?.decimalFormat()
        } // TODO: currency from settings?
      val priceChange =
        remember(marketData, chartPeriod) {
          with(marketData) {
            when (chartPeriod) {
              MarketChartDaysPeriod.DAY -> priceChangePercentage24h
              MarketChartDaysPeriod.WEEK -> priceChangePercentage7d
              MarketChartDaysPeriod.MONTH -> priceChangePercentage30d
              MarketChartDaysPeriod.DAYS_200 -> priceChangePercentage200d
              MarketChartDaysPeriod.MAX -> null
            }?.decimalFormat(signed = true)
          }
        }
      if (price != null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(price)
          if (priceChange != null) Text("$priceChange%")
        }
      }
    }
  }
}
