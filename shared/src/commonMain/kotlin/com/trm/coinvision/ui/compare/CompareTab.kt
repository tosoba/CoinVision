package com.trm.coinvision.ui.compare

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.MR
import dev.icerock.moko.resources.compose.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object CompareTab : Tab {
  @Composable
  override fun Content() {
    Box(Modifier.fillMaxSize()) {
      Text(stringResource(MR.strings.compare), modifier = Modifier.align(Alignment.Center))
    }
  }

  @OptIn(ExperimentalResourceApi::class)
  override val options: TabOptions
    @Composable
    get() {
      val icon = painterResource("compare.xml")
      val title = stringResource(MR.strings.compare)
      return remember { TabOptions(index = 0u, title = title, icon = icon) }
    }
}
