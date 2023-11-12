package com.trm.coinvision.core.cache

import com.trm.coinvision.core.cache.disk.DiskCacheStorage
import com.trm.coinvision.core.util.dataPath
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem

internal actual class PlatformHttpCacheStorageInitializer : HttpCacheStorageInitializer {
  override fun invoke(maxSize: Long): CacheStorage =
    DiskCacheStorage(
      fileSystem = FileSystem.SYSTEM,
      directory = dataPath() / "cache",
      maxSize = maxSize
    )
}
