package com.trm.coinvision.ui.tokensList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.ui.mainSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal object TokensListTab : Tab {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<TokensListScreenModel>()
    val mainSearchBarSize = screenModel.mainSearchBarSizeFlow.collectAsState(null)
    when (val mainSearchBarSizeValue = mainSearchBarSize.value) {
      null -> {
        CoinMarketsColumn(Modifier.fillMaxSize())
      }
      else -> {
        Row(Modifier.fillMaxSize()) {
          Column(Modifier.weight(.5f).fillMaxHeight()) {
            val width = with(LocalDensity.current) { mainSearchBarSizeValue.width.toDp() }
            val height = with(LocalDensity.current) { mainSearchBarSizeValue.height.toDp() }
            Spacer(modifier = Modifier.width(width).height(height).padding(mainSearchBarPadding))
            Spacer(modifier = Modifier.weight(1f))
          }
          CoinMarketsColumn(Modifier.weight(.5f).fillMaxHeight())
        }
      }
    }
  }

  @Composable
  private fun CoinMarketsColumn(modifier: Modifier = Modifier) {
    val screenModel = getScreenModel<TokensListScreenModel>()
    val coinMarkets = screenModel.coinMarkets.collectAsLazyPagingItems()
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(10.dp)) {
      items(coinMarkets.itemCount) { index -> coinMarkets[index]?.let { Text(text = it.name) } }

      with(coinMarkets) {
        when {
          loadState.refresh is LoadState.Loading -> {
            item {
              Box(modifier = Modifier.fillParentMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
              }
            }
          }
          loadState.refresh is LoadState.Error -> {
            item {
              Column(
                modifier = Modifier.fillParentMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(text = LocalStringResources.current.errorOccurred)
                Button(coinMarkets::retry) { Text(text = LocalStringResources.current.retry) }
              }
            }
          }
          loadState.append is LoadState.Loading -> {
            item {
              Box(modifier = Modifier.padding(20.dp)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
              }
            }
          }
          loadState.append is LoadState.Error -> {
            item {
              Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Text(text = LocalStringResources.current.errorOccurred)
                Button(coinMarkets::retry) { Text(text = LocalStringResources.current.retry) }
              }
            }
          }
        }
      }
    }
  }

  @OptIn(ExperimentalResourceApi::class)
  override val options: TabOptions
    @Composable
    get() =
      TabOptions(
        index = 1u,
        title = LocalStringResources.current.list,
        icon = painterResource("list.xml")
      )
}
