package org.mozilla.social.feature.account

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.feature.account.edit.EditAccountViewModel

val accountModule = module {
    viewModel { parametersHolder ->
        AccountViewModel(
            analytics = get(),
            accountRepository = get(),
            loggedInUserAccountId = get(),
            socialDatabase = get(),
            getDetailedAccount = get(),
            navigateTo = get(),
            showSnackbar = get(),
            initialAccountId = parametersHolder[0],
        )
    }
    factory { parametersHolder ->
        AccountTimelineRemoteMediator(
            accountRepository = get(),
            statusRepository = get(),
            socialDatabase = get(),
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