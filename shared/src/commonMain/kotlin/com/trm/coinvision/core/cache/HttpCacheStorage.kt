package com.trm.coinvision.core.cache

import io.ktor.client.plugins.cache.storage.CacheStorage

interface HttpCacheStorageInitializer {
  operator fun invoke(maxSize: Long): CacheStorage
}

internal expect class PlatformHttpCacheStorageInitializer : HttpCacheStorageInitializer

internal const val CACHE_SIZE = 1024L * 1024L * 10L
