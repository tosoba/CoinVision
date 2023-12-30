package com.trm.coinvision.core.common.util.ext

import kotlin.test.Test
import kotlin.test.assertEquals

class DoubleExtTests {
  @Test
  fun formatDecimal() {
    assertEquals("+0.958", .9581.decimalFormat(signed = true))
  }
}
