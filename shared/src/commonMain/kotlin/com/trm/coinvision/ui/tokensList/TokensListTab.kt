package com.trm.coinvision.ui.tokensList

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
import com.trm.coinvision.core.common.di.getScreenModel
import dev.icerock.moko.resources.compose.stringResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object TokensListTab : Tab {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<TokensListScreenModel>()
    Box(Modifier.fillMaxSize()) {
      Text(stringResource(MR.strings.list), modifier = Modifier.align(Alignment.Center))
    }
  }

  @OptIn(ExperimentalResourceApi::class)
  override val options: TabOptions
    @Composable
    get() {
      val icon = painterResource("list.xml")
      val title = stringResource(MR.strings.list)
      return remember { TabOptions(index = 1u, title = title, icon = icon) }
    }
}
