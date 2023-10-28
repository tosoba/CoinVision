package com.trm.coinvision.core.common.util

import platform.Foundation.NSLocale

actual val language: String
  get() = NSLocale.currentLocale.languageCode ?: "en"
