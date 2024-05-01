package social.firefly.feature.account.edit

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.NavigationDestination

fun NavGraphBuilder.editAccountScreen() {
    composable(
        route = NavigationDestination.EditAccount.route,
    ) {
        EditAccountScreen()
    }
}
