package com.trm.coinvision.core.common.util.ext

import cafe.adriel.voyager.navigator.Navigator

internal fun Navigator.root(): Navigator = parent?.root() ?: this
