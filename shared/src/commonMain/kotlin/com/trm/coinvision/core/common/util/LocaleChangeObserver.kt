package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Composable

interface LocaleChangedObserver {
  fun register()

  fun unregister()
}

expect class PlatformLocaleChangedObserver : LocaleChangedObserver

@Composable expect fun PlatformLocaleChangedObserverEffect(onChanged: (String) -> Unit)
