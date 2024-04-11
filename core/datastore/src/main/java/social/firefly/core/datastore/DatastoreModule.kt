package social.firefly.core.datastore

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule =
    module {
        single { UserPreferencesDatastore(androidContext()) }
        single { AppPreferencesDatastore(androidContext()) }
    }
