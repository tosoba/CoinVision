package com.trm.coinvision.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.times
import com.trm.coinvision.ui.mainSearchBarHeight
import com.trm.coinvision.ui.mainSearchBarPadding

@Composable
internal fun TokensSearchBarVerticalSpacer() {
  Spacer(modifier = Modifier.height(mainSearchBarHeight + 2 * mainSearchBarPadding))
}
