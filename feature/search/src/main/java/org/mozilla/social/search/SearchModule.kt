package org.mozilla.social.search

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.core.analytics.SearchAnalytics
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

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
