package org.mozilla.social.feature.account

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.feature.account.edit.EditAccountViewModel

val accountModule = module {
    viewModel { parametersHolder ->
        AccountViewModel(
            analytics = get(),
            getLoggedInUserAccountId = get(),
            socialDatabase = get(),
            getDetailedAccount = get(),
            navigateTo = get(),
            initialAccountId = parametersHolder[0],
            followAccount = get(),
            unfollowAccount = get(),
            blockAccount = get(),
            unblockAccount = get(),
            muteAccount = get(),
            unmuteAccount = get(),
        )
    }
    factory { parametersHolder ->
        AccountTimelineRemoteMediator(
            accountId = parametersHolder[0],
            timelineType = parametersHolder[1],
        )
    }
    viewModel {
        EditAccountViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }
}