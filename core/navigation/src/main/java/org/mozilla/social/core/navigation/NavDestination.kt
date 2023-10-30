package org.mozilla.social.core.navigation

sealed class NavDestination : INavDestination {
    data object Login : NavDestination()
    data class NewPost(val replyId: String?) : NavDestination()
    data class Thread(val statusId: String) : NavDestination()

    data class Report(val accountId: String, val accountHandle: String, val statusId: String) :
        NavDestination()

    data object Feed : NavDestination()

    data class Following(val accountId: String) : NavDestination()
}
