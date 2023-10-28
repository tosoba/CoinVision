package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

actual class PlatformLocaleChangedObserver(onChanged: (String) -> Unit) : LocaleChangedObserver {
  override fun register() {}

  override fun unregister() {}
}

@Composable
actual fun PlatformLocaleChangedObserverEffect(onChanged: (String) -> Unit) {
  DisposableEffect(Unit) {
    val observer = PlatformLocaleChangedObserver(onChanged)
    observer.register()
    onDispose(observer::unregister)
  }
}
