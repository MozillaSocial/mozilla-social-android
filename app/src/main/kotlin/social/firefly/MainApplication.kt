package social.firefly

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.memory.MemoryCache
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.timber.SentryTimberIntegration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.dsl.module
import social.firefly.common.Version
import social.firefly.core.analytics.AppAnalytics
import social.firefly.core.repository.mastodon.AuthCredentialObserver
import social.firefly.core.workmanager.workManagerModule
import social.firefly.feature.account.accountModule
import social.firefly.feature.auth.authModule
import social.firefly.feature.discover.discoverModule
import social.firefly.feature.favorites.favoritesModule
import social.firefly.feature.followers.followersModule
import social.firefly.feature.hashtag.hashTagModule
import social.firefly.feature.media.mediaModule
import social.firefly.feature.notifications.notificationsModule
import social.firefly.feature.report.reportModule
import social.firefly.feature.settings.settingsModule
import social.firefly.feature.thread.threadModule
import social.firefly.feature.feed.feedModule
import social.firefly.feature.search.searchModule
import social.firefly.core.datastore.AppPreferencesDatastore
import social.firefly.core.push.pushModule
import social.firefly.core.ui.chooseAccount.chooseAccountModule
import social.firefly.feature.bookmarks.bookmarksModule
import social.firefly.feature.followedHashTags.followedHashTagsModule
import timber.log.Timber

class MainApplication : Application(), ImageLoaderFactory {

    private lateinit var authCredentialObserver: AuthCredentialObserver
    private val analytics: AppAnalytics by inject()
    private val appPreferencesDatastore: AppPreferencesDatastore by inject()

    override fun onCreate() {
        super.onCreate()
        initializeAppVersion()
        initializeKoin()
        initializeTimberAndSentry()
        initializeAnalytics()
        initializeAuthCredentialInterceptor()
    }

    private fun initializeTimberAndSentry() {
        CoroutineScope(Dispatchers.Default).launch {
            if (appPreferencesDatastore.allowAnalytics.first()) {
                SentryAndroid.init(this@MainApplication) { options ->
                    options.apply {
                        setDiagnosticLevel(SentryLevel.ERROR)
                        dsn = BuildConfig.sentryDsn
                        isDebug = BuildConfig.DEBUG
                        environment = BuildConfig.BUILD_TYPE
                        isEnableUserInteractionTracing = true
                        isAttachScreenshot = false
                        isAttachViewHierarchy = false
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
            } else if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
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
                pushModule,
                chooseAccountModule,
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
                add(ImageDecoderDecoder.Factory())
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
            bookmarksModule,
            discoverModule,
            favoritesModule,
            feedModule,
            followedHashTagsModule,
            followersModule,
            hashTagModule,
            mediaModule,
            social.firefly.feature.post.newPostModule,
            notificationsModule,
            reportModule,
            searchModule,
            settingsModule,
            threadModule,
        )
    }
