package org.mozilla.social.core.usecase.mastodon

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import org.mozilla.social.common.appscope.AppScope
import org.mozilla.social.core.storage.mastodon.mastodonStorageModule
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.FollowAccount
import org.mozilla.social.core.usecase.mastodon.account.GetDetailedAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount
import org.mozilla.social.core.usecase.mastodon.account.UnmuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UpdateMyAccount
import org.mozilla.social.core.usecase.mastodon.auth.IsSignedInFlow
import org.mozilla.social.core.usecase.mastodon.auth.Login
import org.mozilla.social.core.usecase.mastodon.auth.Logout
import org.mozilla.social.core.usecase.mastodon.auth.OpenLoginCustomTab
import org.mozilla.social.core.usecase.mastodon.remotemediators.HashTagTimelineRemoteMediator
import org.mozilla.social.core.usecase.mastodon.report.Report
import org.mozilla.social.core.usecase.mastodon.status.BoostStatus
import org.mozilla.social.core.usecase.mastodon.status.DeleteStatusFromTimelines
import org.mozilla.social.core.usecase.mastodon.status.FavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.PostStatus
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusesToDatabase
import org.mozilla.social.core.usecase.mastodon.status.UndoBoostStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoFavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.VoteOnPoll
import org.mozilla.social.core.usecase.mastodon.thread.GetThreadUseCase

val mastodonUsecaseModule = module {
    factory { parametersHolder ->
        HashTagTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
            parametersHolder[0]
        )
    }
    single {
        GetThreadUseCase(
            statusRepository = get(),
            localStatusRepository = get(),
            saveStatusesToDatabase = get()
        )
    }
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
    single {
        GetDetailedAccount(
            accountRepository = get(),
            localAccountRepository = get(),
            localRelationshipRepository = get(),
            databaseDelegate = get()
        )
    }
    single { GetLoggedInUserAccountId(get()) }

    single {
        BlockAccount(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            accountRepository = get(),
            socialDatabase = get(),
        )
    }
    single {
        FollowAccount(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            accountRepository = get(),
            socialDatabase = get(),
        )
    }
    single {
        MuteAccount(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            accountRepository = get(),
            socialDatabase = get(),
        )
    }
    single {
        UnblockAccount(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            accountRepository = get(),
            socialDatabase = get(),
        )
    }
    single {
        UnfollowAccount(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            accountRepository = get(),
            socialDatabase = get(),
        )
    }
    single {
        UnmuteAccount(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            accountRepository = get(),
            socialDatabase = get(),
        )
    }
    single {
        UpdateMyAccount(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            accountRepository = get(),
            localAccountRepository = get(),
        )
    }
    single {
        Report(
            externalScope = get<AppScope>(),
            showSnackbar = get(),
            reportRepository = get(),
        )
    }
    single {
        PostStatus(
            externalScope = get<AppScope>(),
            mediaApi = get(),
            statusRepository = get(),
            timelineRepository = get(),
            showSnackbar = get(),
            saveStatusesToDatabase = get(),
            localTimelineRepository = get(),
        )
    }
    single {
        BoostStatus(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            saveStatusesToDatabase = get(),
            databaseDelegate = get(),
            localStatusRepository = get(),
        )
    }
    single {
        FavoriteStatus(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            socialDatabase = get(),
            saveStatusesToDatabase = get(),
        )
    }
    single {
        UndoBoostStatus(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            localStatusRepository = get(),
            databaseDelegate = get(),
            saveStatusesToDatabase = get(),
            dispatcherIo = get(),
        )
    }
    single {
        UndoFavoriteStatus(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            localStatusRepository = get(),
            saveStatusesToDatabase = get(),
            databaseDelegate = get(),
            dispatcherIo = get(),
        )
    }
    single {
        VoteOnPoll(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            socialDatabase = get(),
        )
    }
    single {
        DeleteStatusFromTimelines(
            localTimelineRepository = get(),
            showSnackbar = get(),
        )
    }
    single {
        SaveStatusesToDatabase(get(), get(), get(), get())
    }
    single { Dispatchers.IO }
    includes(mastodonStorageModule)
}