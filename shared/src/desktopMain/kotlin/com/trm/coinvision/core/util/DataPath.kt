package com.trm.coinvision.core.util

import java.io.File
import kotlin.jvm.optionals.getOrNull
import okio.Path
import okio.Path.Companion.toPath
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

internal fun dataPath(): Path {
  val appName =
    ProcessHandle.current().info().command().getOrNull()?.split(File.separator)?.lastOrNull()
      ?: "Java App"
  return when (hostOs) {
    OS.Windows -> System.getenv("APPDATA").toPath() / appName
    OS.MacOS -> System.getProperty("user.home").toPath() / "Library/Application Support" / appName
    OS.Linux -> System.getProperty("user.home").toPath() / ".$appName"
    else -> System.getProperty("user.dir", appName).toPath()
  }
}
