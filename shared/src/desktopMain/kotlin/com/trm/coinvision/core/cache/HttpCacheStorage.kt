package com.trm.coinvision.core.cache

import com.trm.coinvision.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.CacheStorage
import java.io.File
import kotlin.jvm.optionals.getOrNull
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

private fun dataPath(): Path {
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

internal actual class PlatformHttpCacheStorageInitializer : HttpCacheStorageInitializer {
  override fun invoke(maxSize: Long): CacheStorage =
    DiskCacheStorage(
      fileSystem = FileSystem.SYSTEM,
      directory = dataPath() / "cache",
      maxSize = maxSize
    )
}
