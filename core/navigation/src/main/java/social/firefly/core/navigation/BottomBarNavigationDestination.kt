package social.firefly.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

sealed class BottomBarNavigationDestination(val route: String) {
    data object Feed : BottomBarNavigationDestination(
        route = "feed",
    ) {
        fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
            this.navigate(route, navOptions)
        }
    }

    data object Discover : BottomBarNavigationDestination(
        route = "discover",
    ) {
        fun NavController.navigateToDiscover(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data object MyAccount : BottomBarNavigationDestination(route = "myAccount") {
        fun NavController.navigateToMyAccount(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }

    data object Notifications : BottomBarNavigationDestination(
        route = "notifications",
    ) {
        fun NavController.navigateToNotificationsScreen(navOptions: NavOptions? = null) {
            navigate(route, navOptions)
        }
    }
}
