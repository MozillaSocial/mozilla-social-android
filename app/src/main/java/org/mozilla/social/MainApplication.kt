package org.mozilla.social

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.repository.mastodon.AuthCredentialObserver
import org.mozilla.social.feature.account.accountModule
import org.mozilla.social.feature.auth.authModule
import org.mozilla.social.feature.discover.discoverModule
import org.mozilla.social.feature.followers.followersModule
import org.mozilla.social.feature.hashtag.hashTagModule
import org.mozilla.social.feature.report.reportModule
import org.mozilla.social.feature.settings.settingsModule
import org.mozilla.social.feature.thread.threadModule
import org.mozilla.social.feed.feedModule
import org.mozilla.social.post.newPostModule
import org.mozilla.social.search.searchModule

class MainApplication : Application(), ImageLoaderFactory {
    private lateinit var authCredentialObserver: AuthCredentialObserver

    private val analytics: Analytics by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                featureModules,
                mainModule,
            )
        }

        analytics.initialize(applicationContext)

        authCredentialObserver = get()
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

val featureModules =
    module {
        includes(
            accountModule,
            authModule,
            discoverModule,
            feedModule,
            followersModule,
            hashTagModule,
            newPostModule,
            reportModule,
            searchModule,
            settingsModule,
            threadModule,
        )
    }
