package com.trm.coinvision.ui.portfolio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.util.LocalStringResources
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object PortfolioTab : Tab {
  @Composable
  override fun Content() {
    Box(Modifier.fillMaxSize()) { Text("Portfolio", modifier = Modifier.align(Alignment.Center)) }
  }

  @OptIn(ExperimentalResourceApi::class)
  override val options: TabOptions
    @Composable
    get() =
      TabOptions(
        index = 2u,
        title = LocalStringResources.current.portfolio,
        icon = painterResource("portfolio.xml")
      )
}
