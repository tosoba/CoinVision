package com.trm.coinvision.ui.common

import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.imageBitmapDecoder
import io.kamel.image.config.imageVectorDecoder

val kamelConfig = KamelConfig {
  takeFrom(KamelConfig.Core)
  imageBitmapDecoder()
  imageVectorDecoder()
}
