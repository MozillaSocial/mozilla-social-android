package social.firefly.core.analytics

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.core.analytics.core.Analytics
import social.firefly.core.analytics.core.DummyAnalytics
import social.firefly.core.analytics.glean.GleanAnalytics
import social.firefly.core.datastore.dataStoreModule

val analyticsModule =
    module {
        includes(
            dataStoreModule,
        )
        single<Analytics> {
            if (BuildConfig.DEBUG) {
                DummyAnalytics()
            } else {
                GleanAnalytics(get())
            }
        }

        singleOf(::AppAnalytics)
        singleOf(::DiscoverAnalytics)
        singleOf(::PostCardAnalytics)
        singleOf(::ChooseServerAnalytics)
        singleOf(::LoginAnalytics)
        singleOf(::FollowersAnalytics)
        singleOf(::NewPostAnalytics)
        singleOf(::FeedAnalytics)
        singleOf(::SearchAnalytics)
        singleOf(::ReportScreenAnalytics)
        singleOf(::SettingsAnalytics)
        singleOf(::ThreadAnalytics)
        singleOf(::AccountAnalytics)
        singleOf(::HashtagAnalytics)
    }
