package com.trm.coinvision

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import com.trm.coinvision.core.common.util.LocalStringResources
import com.trm.coinvision.core.common.util.PlatformLocaleChangedObserverEffect
import com.trm.coinvision.core.common.util.stringResources
import com.trm.coinvision.ui.MainScreen

@Composable
fun App() {
  var stringResources by remember { mutableStateOf(stringResources()) }
  PlatformLocaleChangedObserverEffect { stringResources = stringResources(it) }

  CompositionLocalProvider(LocalStringResources provides stringResources) {
    MaterialTheme { Navigator(MainScreen) }
  }
}
