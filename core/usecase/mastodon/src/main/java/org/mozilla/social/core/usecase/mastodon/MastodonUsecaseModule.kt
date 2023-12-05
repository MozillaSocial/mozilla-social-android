package org.mozilla.social.core.usecase.mastodon

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.appscope.AppScope
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
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
import org.mozilla.social.core.usecase.mastodon.remotemediators.BlocksListPagingSource
import org.mozilla.social.core.usecase.mastodon.remotemediators.FollowersRemoteMediator
import org.mozilla.social.core.usecase.mastodon.remotemediators.FollowingsRemoteMediator
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

val mastodonUsecaseModule =
    module {
        includes(
            mastodonRepositoryModule,
            analyticsModule,
            navigationModule,
        )

        factory { parametersHolder ->
            HashTagTimelineRemoteMediator(
                get(),
                get(),
                get(),
                get(),
                parametersHolder[0],
            )
        }
        single { GetThread(get(), get()) }
        single { Login(get(), get(), get(), get(), get(), get(), get()) }
        single {
            Logout(
                userPreferencesDatastore = get(),
                analytics = get(),
                appScope = get(),
                databaseDelegate = get(),
            )
        }
        single { IsSignedInFlow(get()) }
        singleOf(::GetDetailedAccount)
        single { GetLoggedInUserAccountId(get()) }

        single {
            BlockAccount(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                accountRepository = get(),
                relationshipRepository = get(),
                timelineRepository = get(),
            )
        }
        single {
            FollowAccount(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                accountRepository = get(),
                relationshipRepository = get(),
                databaseDelegate = get(),
            )
        }
        single {
            MuteAccount(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                accountRepository = get(),
                relationshipRepository = get(),
                timelineRepository = get(),
            )
        }
        single {
            UnblockAccount(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                accountRepository = get(),
                relationshipRepository = get(),
            )
        }
        single {
            UnfollowAccount(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                accountRepository = get(),
                relationshipRepository = get(),
                timelineRepository = get(),
                databaseDelegate = get(),
            )
        }
        single {
            UnmuteAccount(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                accountRepository = get(),
                relationshipRepository = get(),
            )
        }
        single {
            UpdateMyAccount(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                accountRepository = get(),
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
                saveStatusToDatabase = get(),
                databaseDelegate = get(),
            )
        }
        single {
            UndoBoostStatus(
                externalScope = get<AppScope>(),
                statusRepository = get(),
                showSnackbar = get(),
                saveStatusToDatabase = get(),
                databaseDelegate = get(),
            )
        }
        single {
            UndoFavoriteStatus(
                externalScope = get<AppScope>(),
                statusRepository = get(),
                showSnackbar = get(),
                saveStatusToDatabase = get(),
                databaseDelegate = get(),
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
                timelineRepository = get(),
                databaseDelegate = get(),
            )
        }

        singleOf(::SaveStatusToDatabase)

        single { RefreshLocalTimeline(get(), get(), get(), get()) }
        single { RefreshFederatedTimeline(get(), get(), get(), get()) }
        singleOf(::RefreshHomeTimeline)
        factory {
            RefreshAccountTimeline(
                accountRepository = get(),
                saveStatusToDatabase = get(),
                databaseDelegate = get(),
                timelineRepository = get(),
                accountId = it[0],
                timelineType = it[1],
            )
        }
        factory {
            FollowersRemoteMediator(
                accountRepository = get(),
                databaseDelegate = get(),
                followersRepository = get(),
                relationshipRepository = get(),
                accountId = it[0],
            )
        }
        factory {
            FollowingsRemoteMediator(
                accountRepository = get(),
                databaseDelegate = get(),
                followingsRepository = get(),
                relationshipRepository = get(),
                accountId = it[0],
            )
        }

        factoryOf(::BlocksListPagingSource)
    }
