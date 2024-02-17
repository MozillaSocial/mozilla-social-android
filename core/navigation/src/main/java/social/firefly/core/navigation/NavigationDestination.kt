package social.firefly.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import social.firefly.core.model.Attachment

/**
 * Represents a top-level Navigation destination
 */
sealed class NavigationDestination(
    val route: String,
) {
    data class Account(val accountId: String) : NavigationDestination(route = ROUTE) {
        fun NavController.navigateToAccount(navOptions: NavOptions? = null) {
            navigate(route(accountIdValue = accountId), navOptions)
        }

        companion object {
            private const val ROUTE = "account"
            const val NAV_PARAM_ACCOUNT_ID = "accountId"
            val fullRoute: String = route("{$NAV_PARAM_ACCOUNT_ID}")

            private fun route(accountIdValue: String) = "$ROUTE?$NAV_PARAM_ACCOUNT_ID=$accountIdValue"
        }
    }

    data object EditAccount : NavigationDestination(
        route = "editAccount",
    ) {
        fun NavController.navigateToEditAccount(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data object Auth : NavigationDestination(
        route = "auth",
    ) {
        fun NavController.navigateToAuthFlow(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data object Tabs : NavigationDestination(
        route = "tabs",
    ) {
        fun NavController.navigateToTabs(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data object Favorites : NavigationDestination(
        route = "favorites"
    ) {
        fun NavController.navigateToFavorites(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data class Followers(
        val accountId: String,
        val displayName: String,
        val startingTab: StartingTab,
    ) : NavigationDestination(
        route = ROUTE,
    ) {
        fun NavController.navigateToFollowing(navOptions: NavOptions? = null) {
            navigate(route(accountId, displayName, startingTab.value), navOptions)
        }

        enum class StartingTab(
            val value: String,
        ) {
            FOLLOWERS("followers"),
            FOLLOWING("following"),
        }

        companion object {
            private const val ROUTE = "followers"
            const val NAV_PARAM_ACCOUNT_ID = "accountId"
            const val NAV_PARAM_DISPLAY_NAME = "displayName"
            const val NAV_PARAM_STARTING_TAB = "startingTab"

            val fullRoute = route(
                accountId = "{$NAV_PARAM_ACCOUNT_ID}",
                displayName = "{$NAV_PARAM_DISPLAY_NAME}",
                startingTab = "{$NAV_PARAM_STARTING_TAB}"
            )

            fun route(
                accountId: String,
                displayName: String,
                startingTab: String,
            ): String = "$ROUTE?" +
                    "$NAV_PARAM_ACCOUNT_ID=$accountId" +
                    "&$NAV_PARAM_DISPLAY_NAME=$displayName" +
                    "&$NAV_PARAM_STARTING_TAB=$startingTab"
        }
    }

    data class HashTag(val hashtag: String) : NavigationDestination(
        route = ROUTE,
    ) {
        fun NavController.navigateToHashTag(navOptions: NavOptions? = null) {
            navigate(route(hashtag), navOptions)
        }

        companion object {
            private const val ROUTE = "hashtag"
            const val NAV_PARAM_HASH_TAG = "hashTagValue"
            val fullRoute = route("{$NAV_PARAM_HASH_TAG}")

            private fun route(paramValue: String) = "$ROUTE?$NAV_PARAM_HASH_TAG=$paramValue"
        }
    }

    data class Media(
        val attachments: List<Attachment>,
        val startIndex: Int = 0,
    ) : NavigationDestination(
        route = ROUTE,
    ) {
        fun NavController.navigateToMedia(navOptions: NavOptions? = null) {
            val mediaBundle = MediaBundle(
                // remove blur hashes because the characters in the hash can mess up serialization
                attachments = attachments.map {
                    when (it) {
                        is Attachment.Image -> it.copy(blurHash = null)
                        is Attachment.Audio -> it.copy(blurHash = null)
                        is Attachment.Gifv -> it.copy(blurHash = null)
                        is Attachment.Video -> it.copy(blurHash = null)
                        is Attachment.Unknown -> it.copy(blurHash = null)
                    }
                },
                startIndex = startIndex,
            )
            navigate(route(Json.encodeToString(mediaBundle)), navOptions)
        }

        @Serializable
        data class MediaBundle(
            val attachments: List<Attachment>,
            val startIndex: Int = 0,
        )

        companion object {
            private const val ROUTE = "media"
            const val NAV_PARAM_BUNDLE = "bundle"
            val fullRoute = route("{$NAV_PARAM_BUNDLE}")

            private fun route(paramValue: String) = "$ROUTE?$NAV_PARAM_BUNDLE=$paramValue"
        }
    }

    data class NewPost(val replyStatusId: String? = null, val editStatusId: String? = null) : NavigationDestination(
        route = ROUTE,
    ) {
        fun NavController.navigateToNewPost(navOptions: NavOptions? = null) {
            navigate(route(replyStatusId, editStatusId), navOptions)
        }

        companion object {
            private const val ROUTE = "newPost"
            const val NAV_PARAM_REPLY_STATUS_ID = "replyStatusId"
            const val NAV_PARAM_EDIT_STATUS_ID = "editStatusId"
            val fullRoute = "$ROUTE?" +
                    "$NAV_PARAM_REPLY_STATUS_ID={$NAV_PARAM_REPLY_STATUS_ID}" +
                    "&$NAV_PARAM_EDIT_STATUS_ID={$NAV_PARAM_EDIT_STATUS_ID}"

            fun route(replyStatusId: String? = null, editStatusId: String? = null): String {
                return when {
                    replyStatusId != null -> "$ROUTE?$NAV_PARAM_REPLY_STATUS_ID=$replyStatusId"
                    editStatusId != null -> "$ROUTE?$NAV_PARAM_EDIT_STATUS_ID=$editStatusId"
                    else -> ROUTE
                }
            }
        }
    }

    data class Report(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val reportStatusId: String? = null,
    ) : NavigationDestination(
            route = ROUTE,
        ) {
        fun NavController.navigateToReport(navOptions: NavOptions? = null) {
            navigate(
                route(
                    reportAccountId = reportAccountId,
                    reportAccountHandle = reportAccountHandle,
                    reportStatusId = reportStatusId,
                ),
                navOptions,
            )
        }

        companion object {
            private const val ROUTE = "report"
            const val NAV_PARAM_REPORT_STATUS_ID = "reportStatusId"
            const val NAV_PARAM_REPORT_ACCOUNT_ID = "reportAccountId"
            const val NAV_PARAM_REPORT_ACCOUNT_HANDLE = "reportAccountHandle"
            val fullRoute =
                "$ROUTE?" +
                    "$NAV_PARAM_REPORT_STATUS_ID={$NAV_PARAM_REPORT_STATUS_ID}" +
                    "&$NAV_PARAM_REPORT_ACCOUNT_ID={$NAV_PARAM_REPORT_ACCOUNT_ID}" +
                    "&$NAV_PARAM_REPORT_ACCOUNT_HANDLE={$NAV_PARAM_REPORT_ACCOUNT_HANDLE}"

            fun route(
                reportAccountId: String,
                reportAccountHandle: String,
                reportStatusId: String? = null,
            ): String =
                "$ROUTE?" +
                    "$NAV_PARAM_REPORT_ACCOUNT_ID=$reportAccountId" +
                    "&$NAV_PARAM_REPORT_ACCOUNT_HANDLE=$reportAccountHandle" +
                    if (reportStatusId != null) {
                        "&$NAV_PARAM_REPORT_STATUS_ID=$reportStatusId"
                    } else {
                        ""
                    }
        }
    }

    data object Search : NavigationDestination(
        route = "search",
    ) {
        fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data object Settings : NavigationDestination(
        route = "settings",
    ) {
        fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data class Thread(val threadStatusId: String) : NavigationDestination(
        route = ROUTE,
    ) {
        fun NavController.navigateToThread(navOptions: NavOptions? = null) {
            navigate(route(threadStatusId), navOptions)
        }

        companion object {
            private const val ROUTE = "thread"
            const val NAV_PARAM_STATUS_ID = "statusId"
            val fullRoute = route("{$NAV_PARAM_STATUS_ID}")

            fun route(statusIdValue: String) = "$ROUTE?$NAV_PARAM_STATUS_ID=$statusIdValue"
        }
    }
}
