package social.firefly.core.usecase.mastodon

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.common.appscope.AppScope
import social.firefly.core.accounts.accountsModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.account.BlockAccount
import social.firefly.core.usecase.mastodon.account.BlockDomain
import social.firefly.core.usecase.mastodon.account.FollowAccount
import social.firefly.core.usecase.mastodon.account.GetDetailedAccount
import social.firefly.core.usecase.mastodon.account.GetDomain
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.account.MuteAccount
import social.firefly.core.usecase.mastodon.account.UnblockAccount
import social.firefly.core.usecase.mastodon.account.UnblockDomain
import social.firefly.core.usecase.mastodon.account.UnfollowAccount
import social.firefly.core.usecase.mastodon.account.UnmuteAccount
import social.firefly.core.usecase.mastodon.account.UpdateMyAccount
import social.firefly.core.usecase.mastodon.auth.Login
import social.firefly.core.usecase.mastodon.auth.Logout
import social.firefly.core.usecase.mastodon.auth.LogoutOfAllAccounts
import social.firefly.core.usecase.mastodon.auth.SwitchActiveAccount
import social.firefly.core.usecase.mastodon.auth.UpdateAllLoggedInAccounts
import social.firefly.core.usecase.mastodon.followRequest.AcceptFollowRequest
import social.firefly.core.usecase.mastodon.followRequest.DenyFollowRequest
import social.firefly.core.usecase.mastodon.hashtag.FollowHashTag
import social.firefly.core.usecase.mastodon.hashtag.GetHashTag
import social.firefly.core.usecase.mastodon.hashtag.UnfollowHashTag
import social.firefly.core.usecase.mastodon.notification.SaveNotificationsToDatabase
import social.firefly.core.usecase.mastodon.report.Report
import social.firefly.core.usecase.mastodon.search.SearchAll
import social.firefly.core.usecase.mastodon.status.BookmarkStatus
import social.firefly.core.usecase.mastodon.status.BoostStatus
import social.firefly.core.usecase.mastodon.status.DeleteStatus
import social.firefly.core.usecase.mastodon.status.EditStatus
import social.firefly.core.usecase.mastodon.status.FavoriteStatus
import social.firefly.core.usecase.mastodon.status.GetInReplyToAccountNames
import social.firefly.core.usecase.mastodon.status.PostStatus
import social.firefly.core.usecase.mastodon.status.SaveStatusToDatabase
import social.firefly.core.usecase.mastodon.status.UndoBookmarkStatus
import social.firefly.core.usecase.mastodon.status.UndoBoostStatus
import social.firefly.core.usecase.mastodon.status.UndoFavoriteStatus
import social.firefly.core.usecase.mastodon.status.VoteOnPoll
import social.firefly.core.usecase.mastodon.thread.GetThread

val mastodonUsecaseModule =
    module {
        includes(
            mastodonRepositoryModule,
            analyticsModule,
            navigationModule,
            accountsModule,
        )

        // factory because it holds global variables
        factoryOf(::Login)

        singleOf(::SaveStatusToDatabase)
        singleOf(::GetDomain)
        singleOf(::SearchAll)
        singleOf(::GetInReplyToAccountNames)
        singleOf(::SaveNotificationsToDatabase)
        singleOf(::UpdateAllLoggedInAccounts)
        singleOf(::SwitchActiveAccount)
        singleOf(::GetThread)
        singleOf(::GetDetailedAccount)
        singleOf(::GetLoggedInUserAccountId)

        single {
            Logout(
                appScope = get(),
                databaseDelegate = get(),
                navigateTo = get(),
                accountsManager = get(),
            )
        }
        single {
            LogoutOfAllAccounts(
                appScope = get(),
                databaseDelegate = get(),
                navigateTo = get(),
                accountsManager = get(),
            )
        }

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
                getThread = get(),
                timelineRepository = get(),
                showSnackbar = get(),
            )
        }
        single {
            EditStatus(
                externalScope = get<AppScope>(),
                statusRepository = get(),
                saveStatusToDatabase = get(),
                timelineRepository = get(),
                showSnackbar = get(),
            )
        }
        single {
            BoostStatus(
                externalScope = get<AppScope>(),
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
            BookmarkStatus(
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
            UndoBookmarkStatus(
                externalScope = get<AppScope>(),
                statusRepository = get(),
                showSnackbar = get(),
                saveStatusToDatabase = get(),
                databaseDelegate = get(),
                bookmarksRepository = get(),
            )
        }
        single {
            VoteOnPoll(
                externalScope = get<AppScope>(),
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

        single {
            AcceptFollowRequest(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                followRequestRepository = get(),
                notificationsRepository = get(),
                relationshipRepository = get(),
            )
        }

        single {
            DenyFollowRequest(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                followRequestRepository = get(),
                notificationsRepository = get(),
                relationshipRepository = get(),
                saveNotificationsToDatabase = get(),
                databaseDelegate = get(),
            )
        }

        single {
            GetHashTag(
                hashtagRepository = get(),
            )
        }

        single {
            BlockDomain(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                domainBlocksRepository = get(),
                relationshipRepository = get(),
                timelineRepository = get(),
            )
        }

        single {
            UnblockDomain(
                externalScope = get<AppScope>(),
                showSnackbar = get(),
                domainBlocksRepository = get(),
                relationshipRepository = get(),
            )
        }
    }
