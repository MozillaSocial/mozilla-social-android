package social.firefly.search

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.core.analytics.SearchAnalytics
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val searchModule = module {
    includes(
        dataStoreModule,
        mastodonRepositoryModule,
        navigationModule,
        mastodonUsecaseModule,
        analyticsModule,
    )

    viewModelOf(::SearchViewModel)
}
