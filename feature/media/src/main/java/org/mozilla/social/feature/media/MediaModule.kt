package org.mozilla.social.feature.media

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val mediaModule = module {
    includes(
        mastodonUsecaseModule,
    )

    viewModel { parameters ->
        MediaViewModel(
            showSnackbar = get(),
            startIndex = parameters[0]
        )
    }
}