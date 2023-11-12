package com.trm.coinvision.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.trm.coinvision.core.util.dataPath

internal actual class PlatformDatastoreInitializer : DatastoreInitializer {
  override fun invoke(): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(produceFile = { dataPath() / DATASTORE_FILE_NAME })
}
