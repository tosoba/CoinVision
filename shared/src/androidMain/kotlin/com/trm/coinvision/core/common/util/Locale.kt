package com.trm.coinvision.core.common.util

import java.util.Locale

actual val language: String
    get() = Locale.getDefault().language ?: "en"
