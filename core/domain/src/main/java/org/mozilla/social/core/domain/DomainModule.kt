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
import org.mozilla.social.core.domain.report.Report
import org.mozilla.social.core.domain.status.BoostStatus
import org.mozilla.social.core.domain.status.DeleteStatus
import org.mozilla.social.core.domain.status.FavoriteStatus
import org.mozilla.social.core.domain.status.PostStatus
import org.mozilla.social.core.domain.status.UndoBoostStatus
import org.mozilla.social.core.domain.status.UndoFavoriteStatus
import org.mozilla.social.core.domain.status.VoteOnPoll

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
    single { Login(get(), get(), get(), get(), get(), get(), get()) }
    single { OpenLoginCustomTab() }
    single {
        Logout(
            userPreferencesDatastore = get(),
            socialDatabase = get(),
            analytics = get(),
            appScope = get(),
        )
    }
    single { IsSignedInFlow(get()) }
    single { GetDetailedAccount(get(), get()) }
    single { GetLoggedInUserAccountId(get()) }

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
    single { Report(
        externalScope = get<AppScope>(),
        showSnackbar = get(),
        reportApi = get(),
    ) }
    single { PostStatus(
        externalScope = get<AppScope>(),
        statusApi = get(),
        mediaApi = get(),
        statusRepository = get(),
        timelineRepository = get(),
        showSnackbar = get(),
    ) }
    single { BoostStatus(
        externalScope = get<AppScope>(),
        statusApi = get(),
        statusRepository = get(),
        showSnackbar = get(),
        socialDatabase = get(),
    ) }
    single { FavoriteStatus(
        externalScope = get<AppScope>(),
        statusApi = get(),
        statusRepository = get(),
        showSnackbar = get(),
        socialDatabase = get(),
    ) }
    single { UndoBoostStatus(
        externalScope = get<AppScope>(),
        statusApi = get(),
        statusRepository = get(),
        showSnackbar = get(),
        socialDatabase = get(),
    ) }
    single { UndoFavoriteStatus(
        externalScope = get<AppScope>(),
        statusApi = get(),
        statusRepository = get(),
        showSnackbar = get(),
        socialDatabase = get(),
    ) }
    single { VoteOnPoll(
        externalScope = get<AppScope>(),
        statusApi = get(),
        showSnackbar = get(),
        socialDatabase = get(),
    ) }
    single { DeleteStatus(
        externalScope = get<AppScope>(),
        statusApi = get(),
        showSnackbar = get(),
        socialDatabase = get(),
    ) }
}