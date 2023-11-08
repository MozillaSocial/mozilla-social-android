package org.mozilla.social.post

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPostModule = module {
    viewModel { parametersHolder ->
        NewPostViewModel(
            replyStatusId = parametersHolder.getOrNull(),
            mediaRepository = get(),
            searchRepository = get(),
            statusRepository = get(),
            timelineRepository = get(),
            popNavBackstack = get(),
            showSnackbar = get(),
            getLoggedInUserAccountId = get(),
            accountRepository = get(),
        )
    }
}