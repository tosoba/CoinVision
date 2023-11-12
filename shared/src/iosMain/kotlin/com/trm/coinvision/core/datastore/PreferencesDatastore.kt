package com.trm.coinvision.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

internal actual class PlatformDatastoreInitializer : DatastoreInitializer {
  override fun invoke(): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
      produceFile = {
        val documentDirectory: NSURL? =
          NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
          )
        requireNotNull(documentDirectory).path + "/$DATASTORE_FILE_NAME"
      }
    )
}
