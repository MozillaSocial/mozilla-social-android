package org.mozilla.social.feature.account.follows

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountFollowUsersModule = module {
    viewModel { parameters -> AccountFollowUsersViewModel(
        get(),
        get()
    ) }
}