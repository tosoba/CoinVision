package com.trm.coinvision.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal actual class PlatformDatastoreInitializer : DatastoreInitializer {
  override fun invoke(): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
      corruptionHandler = null,
      migrations = emptyList(),
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
      produceFile = {
        NSSearchPathForDirectoriesInDomains(
            directory = NSDocumentDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
          )
          .first()
          .toString()
          .toPath() / DATASTORE_FILE_NAME
      }
    )
}
