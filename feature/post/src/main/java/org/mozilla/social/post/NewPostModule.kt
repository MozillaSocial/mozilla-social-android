package org.mozilla.social.post

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPostModule = module {
    viewModel { parametersHolder ->
        NewPostViewModel(
            replyStatusId = parametersHolder.get(),
            accountFlow = get(),
            mediaRepository = get(),
            searchRepository = get(),
            log = get(),
            statusRepository = get(),
            timelineRepository = get(),
            popNavBackstack = get(),
            showSnackbar = get(),
        )
    }
}