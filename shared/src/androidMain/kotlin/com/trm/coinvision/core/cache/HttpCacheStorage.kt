package com.trm.coinvision.core.cache

import com.trm.coinvision.core.cache.disk.DiskCacheStorage
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem
import okio.Path

internal actual class PlatformHttpCacheStorageInitializer(
  private val directory: Path,
) : HttpCacheStorageInitializer {
  override fun invoke(maxSize: Long): CacheStorage =
    DiskCacheStorage(fileSystem = FileSystem.SYSTEM, directory = directory, maxSize = maxSize)
}
