package social.firefly.feature.followedHashTags

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.pagingModule
import social.firefly.core.ui.hashtagcard.hashTagCardModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val followedHashTagsModule = module {
    includes(
        commonModule,
        navigationModule,
        analyticsModule,
        mastodonRepositoryModule,
        mastodonUsecaseModule,
        pagingModule,
        hashTagCardModule,
    )

    viewModelOf(::FollowedHashTagsViewModel)
}