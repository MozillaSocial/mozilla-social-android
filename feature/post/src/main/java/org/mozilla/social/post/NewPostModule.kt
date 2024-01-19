package org.mozilla.social.post

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule
import org.mozilla.social.post.media.MediaDelegate
import org.mozilla.social.post.poll.PollDelegate
import org.mozilla.social.post.status.StatusDelegate

val newPostModule = module {
    includes(
        commonModule,
        mastodonRepositoryModule,
        dataStoreModule,
        mastodonUsecaseModule,
        navigationModule,
        analyticsModule,
    )

    viewModel { parametersHolder ->
        NewPostViewModel(
            analytics = get(),
            replyStatusId = parametersHolder.getOrNull(),
            postStatus = get(),
            popNavBackstack = get(),
            showSnackbar = get(),
            getLoggedInUserAccountId = get(),
            accountRepository = get(),
        )
    }

    factory { parametersHolder ->
        StatusDelegate(
            analytics = get(),
            searchRepository = get(),
            statusRepository = get(),
            coroutineScope = parametersHolder[0],
            inReplyToId = parametersHolder[1],
        )
    }

    factoryOf(::PollDelegate)

    factory { parametersHolder ->
        MediaDelegate(
            coroutineScope = parametersHolder[0],
            mediaRepository = get(),
        )
    }
}
