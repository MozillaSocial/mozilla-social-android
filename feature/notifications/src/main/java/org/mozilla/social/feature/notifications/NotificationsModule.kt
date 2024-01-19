package org.mozilla.social.feature.notifications

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.repository.paging.pagingModule
import org.mozilla.social.core.ui.notifications.notificationUiModule
import org.mozilla.social.core.ui.postcard.postCardModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val notificationsModule = module {
    includes(
        notificationUiModule,
        mastodonRepositoryModule,
        mastodonUsecaseModule,
        pagingModule,
        navigationModule,
        commonModule,
        postCardModule,
        analyticsModule,
    )
    viewModelOf(::NotificationsViewModel)
}