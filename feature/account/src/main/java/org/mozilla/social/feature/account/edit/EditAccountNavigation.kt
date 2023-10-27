package org.mozilla.social.feature.account.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToEditAccount(
    navOptions: NavOptions? = null,
) {
    navigate(NavigationDestination.EditAccount.route, navOptions)
}

fun NavGraphBuilder.editAccountScreen(
    onDone: () -> Unit,
) {
    composable(
        route = NavigationDestination.EditAccount.route,
    ) {
        EditAccountScreen(onDone = onDone)
    }
}