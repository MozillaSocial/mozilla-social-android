package org.mozilla.social.core.usecase.mastodon

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.appscope.AppScope
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
import org.mozilla.social.core.usecase.mastodon.status.DeleteStatus
import org.mozilla.social.core.usecase.mastodon.status.FavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.PostStatus
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusToDatabase
import org.mozilla.social.core.usecase.mastodon.status.UndoBoostStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoFavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.VoteOnPoll
import org.mozilla.social.core.usecase.mastodon.thread.GetThread
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshAccountTimeline
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshFederatedTimeline
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshHomeTimeline
import org.mozilla.social.core.usecase.mastodon.timeline.RefreshLocalTimeline

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
    single { GetThread(get(), get()) }
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
            socialDatabase = get(),
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
            saveStatusToDatabase = get(),
            timelineRepository = get(),
            showSnackbar = get(),

            )
    }
    single {
        BoostStatus(
            externalScope = get(),
            statusRepository = get(),
            saveStatusToDatabase = get(),
            databaseDelegate = get(),
            showSnackbar = get(),
        )
    }
    single {
        FavoriteStatus(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            socialDatabase = get(),
            saveStatusToDatabase = get(),
        )
    }
    single {
        UndoBoostStatus(
            externalScope = get<AppScope>(),
            socialDatabase = get(),
            statusRepository = get(),
            showSnackbar = get(),
            saveStatusToDatabase = get(),
        )
    }
    single {
        UndoFavoriteStatus(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            socialDatabase = get(),
            saveStatusToDatabase = get(),
        )
    }
    single {
        VoteOnPoll(
            externalScope = get(),
            statusRepository = get(),
            pollRepository = get(),
            showSnackbar = get(),
        )
    }
    single {
        DeleteStatus(
            externalScope = get<AppScope>(),
            statusRepository = get(),
            showSnackbar = get(),
            socialDatabase = get(),
        )
    }

    singleOf(::SaveStatusToDatabase)

    single { RefreshLocalTimeline(get(), get(), get(), get()) }
    single { RefreshFederatedTimeline(get(), get(), get(), get()) }
    singleOf(::RefreshHomeTimeline)
    single {
        RefreshAccountTimeline(
            accountRepository = get(),
            socialDatabase = get(),
            saveStatusToDatabase = get(),
            accountId = it[0],
            timelineType = it[1],
        )
    }
}