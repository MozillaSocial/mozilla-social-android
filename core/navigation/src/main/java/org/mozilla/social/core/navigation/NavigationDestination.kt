package org.mozilla.social.core.navigation

sealed class NavigationDestination(
    val route: String,
) {
    data object MyAccount: NavigationDestination(
        route = "myAccount"
    )

    data object Account: NavigationDestination(
        route = "account"
    ) {
        const val NAV_PARAM_ACCOUNT_ID = "accountId"
        val fullRoute = "$route?$NAV_PARAM_ACCOUNT_ID={$NAV_PARAM_ACCOUNT_ID}"

        fun route(accountId: String): String = "$route?$NAV_PARAM_ACCOUNT_ID=$accountId"
    }

    data object Auth: NavigationDestination(
        route = "auth"
    )

    data object Bookmarks: NavigationDestination(
        route = "bookmarks"
    )

    data object Discover: NavigationDestination(
        route = "discover"
    )

    data object Feed: NavigationDestination(
        route = "feed"
    )

    data object Followers: NavigationDestination(
        route = "followers"
    ) {
        const val NAV_PARAM_ACCOUNT_ID = "accountId"
        val fullRoute = "$route?$NAV_PARAM_ACCOUNT_ID={$NAV_PARAM_ACCOUNT_ID}"

        fun route(accountId: String): String = "$route?${NAV_PARAM_ACCOUNT_ID}=$accountId"
    }

    data object Following: NavigationDestination(
        route = "following"
    ) {
        const val NAV_PARAM_ACCOUNT_ID = "accountId"
        val fullRoute = "$route?$NAV_PARAM_ACCOUNT_ID={$NAV_PARAM_ACCOUNT_ID}"

        fun route(accountId: String): String = "$route?${NAV_PARAM_ACCOUNT_ID}=$accountId"
    }

    data object HashTag: NavigationDestination(
        route = "hashTag"
    ) {
        const val NAV_PARAM_HASH_TAG = "hashTagValue"
        val fullRoute = "$route?$NAV_PARAM_HASH_TAG={$NAV_PARAM_HASH_TAG}"

        fun route(hashTagValue: String) = "$route?$NAV_PARAM_HASH_TAG=$hashTagValue"
    }

    data object NewPost: NavigationDestination(
        route = "newPost"
    ) {
        const val NAV_PARAM_REPLY_STATUS_ID = "replyStatusId"
        val fullRoute = "$route?$NAV_PARAM_REPLY_STATUS_ID={$NAV_PARAM_REPLY_STATUS_ID}"

        fun route(replyStatusId: String?): String =
            when {
                replyStatusId != null -> "$route?$NAV_PARAM_REPLY_STATUS_ID=$replyStatusId"
                else -> route
            }
    }

    data object Report: NavigationDestination(
        route = "report"
    ) {
        const val NAV_PARAM_REPORT_STATUS_ID = "reportStatusId"
        const val NAV_PARAM_REPORT_ACCOUNT_ID = "reportAccountId"
        const val NAV_PARAM_REPORT_ACCOUNT_HANDLE = "reportAccountHandle"
        val fullRoute = "$route?" +
                "$NAV_PARAM_REPORT_STATUS_ID={$NAV_PARAM_REPORT_STATUS_ID}" +
                "&$NAV_PARAM_REPORT_ACCOUNT_ID={$NAV_PARAM_REPORT_ACCOUNT_ID}" +
                "&$NAV_PARAM_REPORT_ACCOUNT_HANDLE={$NAV_PARAM_REPORT_ACCOUNT_HANDLE}"

        fun route(
            reportAccountId: String,
            reportAccountHandle: String,
            reportStatusId: String? = null,
        ): String = "$route?" +
                "$NAV_PARAM_REPORT_ACCOUNT_ID=$reportAccountId" +
                "&$NAV_PARAM_REPORT_ACCOUNT_HANDLE=$reportAccountHandle" +
                if (reportStatusId != null) {
                    "&$NAV_PARAM_REPORT_STATUS_ID=$reportStatusId"
                } else {
                    ""
                }
    }

    data object ReportScreen1: NavigationDestination(
        route = "report1"
    )

    data object ReportScreen2: NavigationDestination(
        route = "report2"
    ) {
        const val NAV_PARAM_BUNDLE = "reportDataBundle"
        val fullRoute = "$route?" +
                "$NAV_PARAM_BUNDLE={$NAV_PARAM_BUNDLE}"

        fun route(
            bundle: String
        ): String = "$route?" +
                "$NAV_PARAM_BUNDLE=$bundle"
    }

    data object ReportScreen3: NavigationDestination(
        route = "report3"
    ) {
        const val NAV_PARAM_BUNDLE = "reportDataBundle"
        val fullRoute = "$route?" +
                "$NAV_PARAM_BUNDLE={$NAV_PARAM_BUNDLE}"

        fun route(
            bundle: String
        ): String = "$route?" +
                "$NAV_PARAM_BUNDLE=$bundle"
    }

    data object Search: NavigationDestination(
        route = "search"
    )

    data object Settings: NavigationDestination(
        route = "settings"
    )

    data object Thread: NavigationDestination(
        route = "thread"
    ) {
        const val NAV_PARAM_STATUS_ID = "statusId"
        val fullRoute = "$route?$NAV_PARAM_STATUS_ID={$NAV_PARAM_STATUS_ID}"

        fun route(statusId: String) = "$route?$NAV_PARAM_STATUS_ID=$statusId"
    }
}



