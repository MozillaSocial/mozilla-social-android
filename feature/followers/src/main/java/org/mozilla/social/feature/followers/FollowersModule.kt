package org.mozilla.social.feature.followers

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val followersModule = module {
    viewModel { parameters ->
        FollowersViewModel(
            get(),
            parameters[0],
            parameters[1],
        )
    }
}