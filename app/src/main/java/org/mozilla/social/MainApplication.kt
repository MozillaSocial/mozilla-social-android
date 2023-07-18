package org.mozilla.social

import android.app.Application
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.data.repository.repositoryModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.feature.auth.authModule
import org.mozilla.social.feed.feedModule

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                authModule,
                dataStoreModule,
                commonModule(BuildConfig.DEBUG),
                mainModule,
                feedModule,
                repositoryModule,
            )
        }
    }
}