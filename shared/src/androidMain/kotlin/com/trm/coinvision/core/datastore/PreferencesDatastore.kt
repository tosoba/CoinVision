package com.trm.coinvision.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal actual class PlatformDatastoreInitializer(
  private val path: String,
) : DatastoreInitializer {
  override fun invoke(): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(produceFile = { path.toPath() })
}
