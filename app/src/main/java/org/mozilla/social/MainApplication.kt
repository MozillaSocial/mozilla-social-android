package org.mozilla.social

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.data.AuthTokenObserver
import org.mozilla.social.core.data.repositoryModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.ui.uiModule
import org.mozilla.social.feature.auth.authModule
import org.mozilla.social.feature.settings.settingsModule
import org.mozilla.social.feed.feedModule
import org.mozilla.social.post.newPostModule
import org.mozilla.social.search.searchModule

class MainApplication : Application(), ImageLoaderFactory {

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

    // setup coil
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(VideoFrameDecoder.Factory())
            }
            .build()
}

val appModules = module {
    includes(
        authModule,
        dataStoreModule,
        commonModule(BuildConfig.DEBUG),
        mainModule,
        feedModule,
        searchModule,
        repositoryModule(BuildConfig.DEBUG),
        newPostModule,
        settingsModule,
        uiModule,
    )
}