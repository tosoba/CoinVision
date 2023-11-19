package com.trm.coinvision.ui.compareTokens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBar
import com.trm.coinvision.ui.tokensSearchBar.TokensSearchBarArgs
import com.trm.coinvision.ui.tokensSearchBar.rememberTokensSearchBarArgs
import com.trm.coinvision.ui.tokensSearchBar.tokensSearchBarPadding
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal class CompareTokensTab(private val mainTokensSearchBarArgs: TokensSearchBarArgs) : Tab {
  @Composable
  override fun Content() {
    val screenModel = getScreenModel<CompareTokensScreenModel>()

    val selectedMainToken by screenModel.selectedMainTokenFlow.collectAsState()
    val selectedReferenceToken by screenModel.selectedReferenceTokenFlow.collectAsState()

    val referenceTokensSearchBarArgs =
      rememberTokensSearchBarArgs(screenModel.referenceTokensSearchBarViewModel)

    if (LocalWidthSizeClass.current != WindowWidthSizeClass.Compact) {
      Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBar(mainTokensSearchBarArgs)
          SelectedTokenData(
            modifier = Modifier.fillMaxSize(),
            token = selectedMainToken,
          )
        }

        Column(modifier = Modifier.weight(.5f).fillMaxHeight()) {
          TokensSearchBar(referenceTokensSearchBarArgs)
          SelectedTokenData(
            modifier = Modifier.fillMaxSize(),
            token = selectedReferenceToken,
          )
        }
      }
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        TokensSearchBar(mainTokensSearchBarArgs)
        SelectedTokenData(
          modifier = Modifier.fillMaxWidth().weight(.5f),
          token = selectedMainToken,
        )

        TokensSearchBar(referenceTokensSearchBarArgs)
        SelectedTokenData(
          modifier = Modifier.fillMaxWidth().weight(.5f),
          token = selectedReferenceToken
        )
      }
    }
  }

  @Composable
  private fun TokensSearchBar(args: TokensSearchBarArgs) {
    TokensSearchBar(modifier = Modifier.fillMaxWidth().padding(tokensSearchBarPadding), args = args)
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
