package com.trm.coinvision.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface DatastoreInitializer {
  operator fun invoke(): DataStore<Preferences>
}

internal expect class PlatformDatastoreInitializer : DatastoreInitializer

internal const val DATASTORE_FILE_NAME = "coinvision.preferences_pb"
