package com.trm.coinvision.core.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual class PlatformLocaleChangedObserver(
  private val context: Context,
  private val onChanged: (String) -> Unit
) : LocaleChangedObserver {
  private var receiver: BroadcastReceiver? = null

  override fun register() {
    receiver =
      object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
          if (intent?.action == Intent.ACTION_LOCALE_CHANGED) {
            onChanged(Locale.getDefault().language)
          }
        }
      }
    context.registerReceiver(receiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))
  }

  override fun unregister() {
    receiver?.let(context::unregisterReceiver)
  }
}

@Composable
actual fun PlatformLocaleChangedObserverEffect(onChanged: (String) -> Unit) {
  val context = LocalContext.current
  DisposableEffect(Unit) {
    val observer = PlatformLocaleChangedObserver(context, onChanged)
    observer.register()
    onDispose(observer::unregister)
  }
}
