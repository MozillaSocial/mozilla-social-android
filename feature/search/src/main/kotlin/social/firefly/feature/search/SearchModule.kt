package social.firefly.feature.search

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.ui.hashtagcard.hashTagCardModule
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val searchModule = module {
    includes(
        dataStoreModule,
        mastodonRepositoryModule,
        navigationModule,
        mastodonUsecaseModule,
        analyticsModule,
        hashTagCardModule,
        postCardModule,
    )

    viewModelOf(::SearchViewModel)
}
