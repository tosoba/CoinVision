package com.trm.coinvision

import kotlin.test.Test
import kotlin.test.assertTrue

class IosGreetingTest {
  @Test
  fun testExample() {
    assertTrue(Greeting().greet().contains("iOS"), "Check iOS is mentioned")
  }
}
