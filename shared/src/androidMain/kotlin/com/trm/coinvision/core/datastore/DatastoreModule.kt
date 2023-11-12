package com.trm.coinvision.core.datastore

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val datastoreModule: Module = module {
  single {
    PlatformDatastoreInitializer(
      androidContext().filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
    )()
  }
}
