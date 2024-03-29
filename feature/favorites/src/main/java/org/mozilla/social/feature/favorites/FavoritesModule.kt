package org.mozilla.social.feature.favorites

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.paging.pagingModule
import org.mozilla.social.core.ui.postcard.postCardModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val favoritesModule = module {
    includes(
        commonModule,
        navigationModule,
        mastodonUsecaseModule,
        postCardModule,
        analyticsModule,
        navigationModule,
        pagingModule,
    )

    viewModelOf(::FavoritesViewModel)
}
