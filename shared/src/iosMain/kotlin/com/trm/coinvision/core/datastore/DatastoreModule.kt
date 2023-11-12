package com.trm.coinvision.core.datastore

import org.koin.core.module.Module
import org.koin.dsl.module

actual val datastoreModule: Module = module { single { PlatformDatastoreInitializer()() } }
