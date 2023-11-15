package com.trm.coinvision.core.cache

import com.trm.coinvision.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal actual class PlatformHttpCacheStorageInitializer : HttpCacheStorageInitializer {
  override fun invoke(maxSize: Long): CacheStorage =
    DiskCacheStorage(
      fileSystem = FileSystem.SYSTEM,
      directory =
        NSSearchPathForDirectoriesInDomains(
            directory = NSCachesDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
          )
          .first()
          .toString()
          .toPath(),
      maxSize = maxSize
    )
}
