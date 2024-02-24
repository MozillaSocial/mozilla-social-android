package social.firefly.post

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule
import social.firefly.post.media.MediaDelegate
import social.firefly.post.poll.PollDelegate
import social.firefly.post.status.StatusDelegate

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
            replyStatusId = parametersHolder[0],
            editStatusId = parametersHolder[1],
            postStatus = get(),
            editStatus = get(),
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
            editStatusId = parametersHolder[2]
        )
    }

    factory { parametersHolder ->
        PollDelegate(
            analytics = get(),
            coroutineScope = parametersHolder[0],
            statusRepository = get(),
            editStatusId = parametersHolder[1],
        )
    }

    factory { parametersHolder ->
        MediaDelegate(
            coroutineScope = parametersHolder[0],
            editStatusId = parametersHolder[1],
            mediaRepository = get(),
            statusRepository = get(),
        )
    }
}
