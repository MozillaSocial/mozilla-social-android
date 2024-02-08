package org.mozilla.social

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.memory.MemoryCache
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.timber.SentryTimberIntegration
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mozilla.social.common.Version
import org.mozilla.social.core.analytics.AppAnalytics
import org.mozilla.social.core.repository.mastodon.AuthCredentialObserver
import org.mozilla.social.core.workmanager.workManagerModule
import org.mozilla.social.feature.account.accountModule
import org.mozilla.social.feature.auth.authModule
import org.mozilla.social.feature.discover.discoverModule
import org.mozilla.social.feature.favorites.favoritesModule
import org.mozilla.social.feature.followers.followersModule
import org.mozilla.social.feature.hashtag.hashTagModule
import org.mozilla.social.feature.media.mediaModule
import org.mozilla.social.feature.notifications.notificationsModule
import org.mozilla.social.feature.report.reportModule
import org.mozilla.social.feature.settings.settingsModule
import org.mozilla.social.feature.thread.threadModule
import org.mozilla.social.feed.feedModule
import org.mozilla.social.post.newPostModule
import org.mozilla.social.search.searchModule
import timber.log.Timber

class MainApplication : Application(), ImageLoaderFactory {

    private lateinit var authCredentialObserver: AuthCredentialObserver
    private val analytics: AppAnalytics by inject()

    override fun onCreate() {
        super.onCreate()
        initializeAppVersion()
        initializeTimberAndSentry()
        initializeKoin()
        initializeAnalytics()
        initializeAuthCredentialInterceptor()
    }

    private fun initializeTimberAndSentry() {
        SentryAndroid.init(this) { options ->
            options.apply {
                setDiagnosticLevel(SentryLevel.ERROR)
                dsn = BuildConfig.sentryDsn
                isDebug = BuildConfig.DEBUG
                environment = BuildConfig.BUILD_TYPE
                isEnableUserInteractionTracing = true
                isAttachScreenshot = false
                isAttachViewHierarchy = true
                sampleRate = 1.0
                profilesSampleRate = 1.0
                if (!BuildConfig.DEBUG) {
                    addIntegration(
                        SentryTimberIntegration(
                            minEventLevel = SentryLevel.ERROR,
                            minBreadcrumbLevel = SentryLevel.INFO
                        )
                    )
                } else {
                    Timber.plant(Timber.DebugTree())
                }
            }
        }
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            workManagerFactory()
            modules(
                featureModules,
                mainModule,
                workManagerModule,
            )
        }
    }

    private fun initializeAnalytics() = analytics.initialize(applicationContext)
    private fun initializeAuthCredentialInterceptor() {
        authCredentialObserver = get()
    }

    private fun initializeAppVersion() {
        Version.name = BuildConfig.VERSION_NAME
        Version.code = BuildConfig.VERSION_CODE
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
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(1.0)
                    .build()
            }
            .build()
}

val featureModules =
    module {
        includes(
            accountModule,
            authModule,
            discoverModule,
            favoritesModule,
            feedModule,
            followersModule,
            hashTagModule,
            mediaModule,
            newPostModule,
            notificationsModule,
            reportModule,
            searchModule,
            settingsModule,
            threadModule,
        )
    }
