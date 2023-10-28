package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

actual class PlatformLocaleChangedObserver(
  private val onChanged: (String) -> Unit,
) : LocaleChangedObserver {
  override fun register() {
    // TODO:
    //    val notificationCenter = NSNotificationCenter.defaultCenter
    //    notificationCenter.addObserverForName(
    //      NSLocale.currentLocaleDidChangeNotification,
    //      null,
    //      NSOperationQueue.mainQueue
    //    ) { _ ->
    //      callback()
    //    }
  }

  override fun unregister() {
    // TODO: unregister
  }
}

@Composable
actual fun PlatformLocaleChangedObserverEffect(onChanged: (String) -> Unit) {
  DisposableEffect(Unit) {
    val observer = PlatformLocaleChangedObserver(onChanged)
    observer.register()
    onDispose(observer::unregister)
  }
}
