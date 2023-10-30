package com.trm.coinvision.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import platform.Foundation.NSCurrentLocaleDidChangeNotification
import platform.Foundation.NSLocale
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.darwin.NSObjectProtocol

actual class PlatformLocaleChangedObserver(
  private val onChanged: (String) -> Unit,
) : LocaleChangedObserver {
  private var observer: NSObjectProtocol? = null

  override fun register() {
    observer =
      NSNotificationCenter.defaultCenter.addObserverForName(
        NSCurrentLocaleDidChangeNotification,
        null,
        NSOperationQueue.mainQueue
      ) { _ ->
        onChanged(NSLocale.currentLocale.languageCode)
      }
  }

  override fun unregister() {
    observer?.let(NSNotificationCenter.defaultCenter::removeObserver)
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
