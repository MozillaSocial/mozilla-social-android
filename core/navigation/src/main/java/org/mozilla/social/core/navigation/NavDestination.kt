package org.mozilla.social.core.navigation

/**
 * Nav destinations for use with [EventRelay]
 *
 * @constructor Create empty Nav destination
 */
sealed class NavDestination {
    data object Login : NavDestination()
    data class NewPost(val replyId: String?) : NavDestination()
    data class Thread(val statusId: String) : NavDestination()

    data class Report(val accountId: String, val accountHandle: String, val statusId: String? = null) :
        NavDestination()

    data class Following(val accountId: String) : NavDestination()
    data class Followers(val accountId: String) : NavDestination()
    data object Settings : NavDestination()

    data object MyAccount : NavDestination()
    data class Account(val accountId: String) : NavDestination()
    data object EditAccount : NavDestination()
    data object Tabs: NavDestination()

    data class Hashtag(val hashtag: String) : NavDestination()

}