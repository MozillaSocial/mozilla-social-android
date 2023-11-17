package org.mozilla.social.feature.account.edit

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavGraphBuilder.editAccountScreen() {
    composable(
        route = NavigationDestination.EditAccount.route,
    ) {
        EditAccountScreen()
    }
}
