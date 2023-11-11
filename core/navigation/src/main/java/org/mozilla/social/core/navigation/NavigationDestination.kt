package org.mozilla.social.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * Represents a top-level Navigation destination
 */
sealed class NavigationDestination(
    val route: String,
) {
    data class Account(val accountId: String) : NavigationDestination(route = ROUTE) {
        fun NavController.navigateToAccount(
            navOptions: NavOptions? = null,
        ) {
            navigate(route(accountIdValue = accountId), navOptions)
        }

        companion object {
            private const val ROUTE = "account"
            const val NAV_PARAM_ACCOUNT_ID = "accountId"
            val fullRoute: String = route("{$NAV_PARAM_ACCOUNT_ID}")
            private fun route(accountIdValue: String) =
                "${ROUTE}?$NAV_PARAM_ACCOUNT_ID=$accountIdValue"
        }
    }

    data object EditAccount : NavigationDestination(
        route = "editAccount"
    ) {
        fun NavController.navigateToEditAccount(
            navOptions: NavOptions? = null,
        ) {
            navigate(route, navOptions)
        }
    }

    data object Auth : NavigationDestination(
        route = "auth"
    ) {
        fun NavController.navigateToAuthFlow(navOptions: NavOptions? = null) {
            this.navigate(route, navOptions)
        }
    }

    data object Tabs : NavigationDestination(
        route = "tabs"
    ) {
        fun NavController.navigateToTabs(navOptions: NavOptions? = null) {
            this.navigate(route, navOptions)
        }
    }

    data class Followers(val accountId: String) : NavigationDestination(
        route = ROUTE
    ) {
        fun NavController.navigateToFollowers(
            navOptions: NavOptions? = null,
        ) {
            navigate(route(accountId), navOptions)
        }

        companion object {
            private const val ROUTE = "followers"
            const val NAV_PARAM_ACCOUNT_ID = "accountId"
            val fullRoute = route("{$NAV_PARAM_ACCOUNT_ID}")
            private fun route(paramValue: String) = "${ROUTE}?$NAV_PARAM_ACCOUNT_ID=$paramValue"
        }
    }

    data class Following(val accountId: String) : NavigationDestination(
        route = ROUTE
    ) {

        fun NavController.navigateToFollowing(
            navOptions: NavOptions? = null,
        ) {
            navigate(route(accountId), navOptions)
        }

        companion object {
            private const val ROUTE = "following"
            const val NAV_PARAM_ACCOUNT_ID = "accountId"
            val fullRoute = route("{$NAV_PARAM_ACCOUNT_ID}")
            fun route(accountId: String): String = "$ROUTE?${NAV_PARAM_ACCOUNT_ID}=$accountId"
        }
    }

    data class HashTag(val hashtag: String) : NavigationDestination(
        route = ROUTE
    ) {
        fun NavController.navigateToHashTag(
            navOptions: NavOptions? = null,
        ) {
            navigate(route(hashtag), navOptions)
        }

        companion object {
            private const val ROUTE = "hashtag"
            const val NAV_PARAM_HASH_TAG = "hashTagValue"
            val fullRoute = route("{${NAV_PARAM_HASH_TAG}}")
            private fun route(paramValue: String) =
                "$ROUTE?$NAV_PARAM_HASH_TAG=$paramValue"
        }
    }

    data class NewPost(val replyStatusId: String? = null) : NavigationDestination(
        route = ROUTE,
    ) {
        fun NavController.navigateToNewPost(
            navOptions: NavOptions? = null,
        ) {
            navigate(route(replyStatusId), navOptions)
        }

        companion object {
            private const val ROUTE = "newPost"
            const val NAV_PARAM_REPLY_STATUS_ID = "replyStatusId"
            val fullRoute = route("{$NAV_PARAM_REPLY_STATUS_ID}")

            fun route(replyStatusId: String?): String {
                val a = when {
                    replyStatusId != null -> "$ROUTE?$NAV_PARAM_REPLY_STATUS_ID=$replyStatusId"
                    else -> ROUTE
                }

                println(a)
                return a
            }
        }
    }

    data class Report(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val reportStatusId: String? = null
    ) : NavigationDestination(
        route = ROUTE
    ) {
        fun NavController.navigateToReport(
            navOptions: NavOptions? = null,
        ) {
            navigate(
                route(
                    reportAccountId = reportAccountId,
                    reportAccountHandle = reportAccountHandle,
                    reportStatusId = reportStatusId,
                ),
                navOptions
            )
        }

        companion object {
            private const val ROUTE = "report"
            const val NAV_PARAM_REPORT_STATUS_ID = "reportStatusId"
            const val NAV_PARAM_REPORT_ACCOUNT_ID = "reportAccountId"
            const val NAV_PARAM_REPORT_ACCOUNT_HANDLE = "reportAccountHandle"
            val fullRoute = "$ROUTE?" +
                    "$NAV_PARAM_REPORT_STATUS_ID={$NAV_PARAM_REPORT_STATUS_ID}" +
                    "&$NAV_PARAM_REPORT_ACCOUNT_ID={$NAV_PARAM_REPORT_ACCOUNT_ID}" +
                    "&$NAV_PARAM_REPORT_ACCOUNT_HANDLE={$NAV_PARAM_REPORT_ACCOUNT_HANDLE}"

            fun route(
                reportAccountId: String,
                reportAccountHandle: String,
                reportStatusId: String? = null,
            ): String = "$ROUTE?" +
                    "$NAV_PARAM_REPORT_ACCOUNT_ID=$reportAccountId" +
                    "&$NAV_PARAM_REPORT_ACCOUNT_HANDLE=$reportAccountHandle" +
                    if (reportStatusId != null) {
                        "&$NAV_PARAM_REPORT_STATUS_ID=$reportStatusId"
                    } else {
                        ""
                    }
        }
    }

    data object Settings : NavigationDestination(
        route = "settings"
    ) {
        fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data class Thread(val threadStatusId: String) : NavigationDestination(
        route = ROUTE
    ) {

        fun NavController.navigateToThread(
            navOptions: NavOptions? = null,
        ) {
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