package social.firefly.feature.media

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val mediaModule = module {
    includes(
        mastodonUsecaseModule,
    )

    viewModel { parameters ->
        MediaViewModel(
            showSnackbar = get(),
            statusRepository = get(),
            statusId = parameters[0],
            startIndex = parameters[1]
        )
    }
}