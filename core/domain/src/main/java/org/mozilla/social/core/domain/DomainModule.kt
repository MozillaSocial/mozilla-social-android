package org.mozilla.social.core.domain

import org.koin.dsl.module
import org.mozilla.social.common.appscope.AppScope
import org.mozilla.social.core.domain.account.BlockAccount
import org.mozilla.social.core.domain.account.FollowAccount
import org.mozilla.social.core.domain.account.MuteAccount
import org.mozilla.social.core.domain.account.UnblockAccount
import org.mozilla.social.core.domain.account.UnfollowAccount
import org.mozilla.social.core.domain.account.UnmuteAccount
import org.mozilla.social.core.domain.account.UpdateMyAccount
import org.mozilla.social.core.domain.remotemediators.HashTagTimelineRemoteMediator

val domainModule = module {
    factory { parametersHolder ->
        HashTagTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
            parametersHolder[0]
        )
    }
    single { GetThreadUseCase(get()) }
    single { Login(get(), get(), get(), get(), get()) }
    single {
        Logout(
            userPreferencesDatastore = get(),
            socialDatabase = get(),
            analytics = get(),
            appScope = get(),
        )
    }
    single { IsSignedInFlow(get()) }
    single { AccountIdFlow(get()) }
    single { GetDetailedAccount(get(), get()) }
    single { AccountIdBlocking(get()) }

    single { BlockAccount(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        accountApi = get(),
        socialDatabase = get(),
    ) }
    single { FollowAccount(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        accountApi = get(),
        socialDatabase = get(),
    ) }
    single { MuteAccount(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        accountApi = get(),
        socialDatabase = get(),
    ) }
    single { UnblockAccount(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        accountApi = get(),
        socialDatabase = get(),
    ) }
    single { UnfollowAccount(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        accountApi = get(),
        socialDatabase = get(),
    ) }
    single { UnmuteAccount(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        accountApi = get(),
        socialDatabase = get(),
    ) }
    single { UpdateMyAccount(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        accountApi = get(),
        socialDatabase = get(),
    ) }
}