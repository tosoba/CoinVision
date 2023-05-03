package com.trm.coinvision

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform
