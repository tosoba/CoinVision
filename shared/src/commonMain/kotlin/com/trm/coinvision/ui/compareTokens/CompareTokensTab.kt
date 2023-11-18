package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.trm.coinvision.core.common.di.getScreenModel
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.LocalWidthSizeClass
import com.trm.coinvision.ui.common.SelectedTokenData
import com.trm.coinvision.ui.common.TokensSearchBarVerticalSpacer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal object CompareTokensTab : Tab {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<CompareTokensScreenModel>()
    val token by screenModel.selectedToken.collectAsState()
    if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
      Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBarVerticalSpacer()
          SelectedTokenData(modifier = Modifier.fillMaxSize(), token = token)
        }
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBarVerticalSpacer()
          SelectedTokenData(modifier = Modifier.fillMaxSize(), token = token)
        }
      }
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        TokensSearchBarVerticalSpacer()
        SelectedTokenData(modifier = Modifier.fillMaxWidth().weight(.5f), token = token)
        TokensSearchBarVerticalSpacer()
        SelectedTokenData(modifier = Modifier.fillMaxWidth().weight(.5f), token = token)
      }
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
