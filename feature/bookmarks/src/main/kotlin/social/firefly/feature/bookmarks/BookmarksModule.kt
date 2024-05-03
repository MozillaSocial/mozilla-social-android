package social.firefly.feature.bookmarks

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.paging.pagingModule
import social.firefly.core.ui.postcard.postCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val bookmarksModule = module {
    includes(
        commonModule,
        navigationModule,
        mastodonUsecaseModule,
        postCardModule,
        analyticsModule,
        navigationModule,
        pagingModule,
    )

    viewModelOf(::BookmarksViewModel)
}