package org.mozilla.social.feature.followers

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val followersModule = module {
    viewModel { parameters ->
        FollowersViewModel(
            accountId = parameters[0],
            followerScreenType = parameters[1],
            accountRepository = get(),
            navigateTo = get(),
            analytics = get(),
        )
    }
}