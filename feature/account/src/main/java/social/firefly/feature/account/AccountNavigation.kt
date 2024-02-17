package social.firefly.feature.account

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.accountScreen() {
    composable(
        route = NavigationDestination.Account.fullRoute,
        arguments =
            listOf(
                navArgument(NavigationDestination.Account.NAV_PARAM_ACCOUNT_ID) {
                    nullable = true
                },
            ),
    ) {
        val accountId: String? =
            it.arguments?.getString(NavigationDestination.Account.NAV_PARAM_ACCOUNT_ID)
        AccountScreen(
            accountId = accountId,
        )
    }
}
