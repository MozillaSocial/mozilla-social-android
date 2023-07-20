package org.mozilla.social

import android.app.Application
import org.koin.android.BuildConfig
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.data.AuthTokenObserver
import org.mozilla.social.core.data.repositoryModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.feature.auth.authModule
import org.mozilla.social.feature.settings.settingsModule
import org.mozilla.social.feed.feedModule
import org.mozilla.social.post.newPostModule

class MainApplication : Application() {

    private lateinit var authTokenObserver: AuthTokenObserver

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                appModules,
            )
        }

        authTokenObserver = get()
    }
}

val appModules = module {
    includes(
        authModule,
        dataStoreModule,
        commonModule(BuildConfig.DEBUG),
        mainModule,
        feedModule,
        repositoryModule(BuildConfig.DEBUG),
        newPostModule,
        settingsModule,
    )
}