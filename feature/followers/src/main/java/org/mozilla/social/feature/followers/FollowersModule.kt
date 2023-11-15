package org.mozilla.social.feature.followers

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val followersModule = module {
    viewModel { parameters ->
        FollowersViewModel(
            accountId = parameters[0],
            followingsRepository = get(),
            followersRepository = get(),
            navigateTo = get(),
            analytics = get(),
        )
    }
}