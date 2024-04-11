package social.firefly.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

sealed class AuthNavigationDestination(val route: String) {
    data object Login : AuthNavigationDestination(
        route = "login",
    ) {
        fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
            this.navigate(route, navOptions)
        }
    }

    data object ChooseServer : AuthNavigationDestination(
        route = "chooseServer",
    ) {
        fun NavController.navigateToChooseServerScreen(navOptions: NavOptions? = null) {
            this.navigate(route, navOptions)
        }
    }
}
