package com.trm.coinvision.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

internal enum class MainTab { COMPARE_TOKENS, TOKENS_LIST }

internal class MainNavigatorViewModel : ViewModel() {
  var selectedTab by mutableStateOf(MainTab.COMPARE_TOKENS)
    private set

  fun onTabSelected(tab: MainTab) {
    selectedTab = tab
  }
}
