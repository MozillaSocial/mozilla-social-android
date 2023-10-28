package org.mozilla.social.feature.account

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    viewModel { parametersHolder ->
        AccountViewModel(
            analytics = get(),
            accountRepository = get(),
            accountIdBlocking = get(),
            log = get(),
            statusRepository = get(),
            socialDatabase = get(),
            getDetailedAccount = get(),
            postCardNavigation = get(),
            accountNavigationCallbacks = get(),
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

    single<AccountNavigationCallbacks> { AccountNavigationCallbacksImpl(get()) }
}