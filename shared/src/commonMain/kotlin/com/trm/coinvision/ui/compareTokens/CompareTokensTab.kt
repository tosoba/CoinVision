package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.ui.mainSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal object CompareTokensTab : Tab {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<CompareTokensScreenModel>()
    val searchBarSize by screenModel.mainSearchBarSizeFlow.collectAsState(null)
    Box(Modifier.fillMaxSize()) {
      searchBarSize?.let {
        val width = with(LocalDensity.current) { it.width.toDp() }
        val height = with(LocalDensity.current) { it.height.toDp() }
        Box(modifier = Modifier.width(width).height(height).padding(mainSearchBarPadding))
      }
      Text(LocalStringResources.current.compare, modifier = Modifier.align(Alignment.Center))
    }
  }

  @OptIn(ExperimentalResourceApi::class)
  override val options: TabOptions
    @Composable
    get() =
      TabOptions(
        index = 0u,
        title = LocalStringResources.current.compare,
        icon = painterResource("compare.xml")
      )
}
