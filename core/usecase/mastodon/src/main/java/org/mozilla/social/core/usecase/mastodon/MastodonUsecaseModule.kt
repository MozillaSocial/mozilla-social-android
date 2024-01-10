package org.mozilla.social.core.usecase.mastodon

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.appscope.AppScope
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.FollowAccount
import org.mozilla.social.core.usecase.mastodon.account.GetDetailedAccount
import org.mozilla.social.core.usecase.mastodon.account.GetDomain
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount
import org.mozilla.social.core.usecase.mastodon.account.UnmuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UpdateMyAccount
import org.mozilla.social.core.usecase.mastodon.auth.IsSignedInFlow
import org.mozilla.social.core.usecase.mastodon.auth.Login
import org.mozilla.social.core.usecase.mastodon.auth.Logout
import org.mozilla.social.core.usecase.mastodon.hashtag.FollowHashTag
import org.mozilla.social.core.usecase.mastodon.hashtag.GetHashTag
import org.mozilla.social.core.usecase.mastodon.hashtag.UnfollowHashTag
import org.mozilla.social.core.usecase.mastodon.notification.SaveNotificationsToDatabase
import org.mozilla.social.core.usecase.mastodon.report.Report
import org.mozilla.social.core.usecase.mastodon.search.SearchAll
import org.mozilla.social.core.usecase.mastodon.status.BoostStatus
import org.mozilla.social.core.usecase.mastodon.status.DeleteStatus
import org.mozilla.social.core.usecase.mastodon.status.FavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.GetInReplyToAccountNames
import org.mozilla.social.core.usecase.mastodon.status.PostStatus
import org.mozilla.social.core.usecase.mastodon.status.SaveStatusToDatabase
import org.mozilla.social.core.usecase.mastodon.status.UndoBoostStatus
import org.mozilla.social.core.usecase.mastodon.status.UndoFavoriteStatus
import org.mozilla.social.core.usecase.mastodon.status.VoteOnPoll
import org.mozilla.social.core.usecase.mastodon.thread.GetThread

val mastodonUsecaseModule =
    module {
        includes(
            mastodonRepositoryModule,
            analyticsModule,
            navigationModule,
        )

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
                favoritesRepository = get(),
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

        single {
            FollowHashTag(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                hashtagRepository = get(),
            )
        }

        single {
            UnfollowHashTag(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                hashtagRepository = get(),
            )
        }

        singleOf(::SaveStatusToDatabase)

        singleOf(::GetDomain)
        singleOf(::SearchAll)
        singleOf(::GetInReplyToAccountNames)
        singleOf(::SaveNotificationsToDatabase)

        single {
            GetHashTag(
                hashtagRepository = get(),
            )
        }
    }
