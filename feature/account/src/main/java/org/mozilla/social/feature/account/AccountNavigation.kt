package org.mozilla.social.feature.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.postcard.PostCardNavigation

fun NavController.navigateToAccount(
    navOptions: NavOptions? = null,
    accountId: String,
) {
    navigate(NavigationDestination.Account.route(accountId), navOptions)
}

// The custom screen here doesn't seem to be used anywhere, so I'm going to 
fun NavGraphBuilder.accountScreen(
    accountNavigationCallbacks: AccountNavigationCallbacks,
    postCardNavigation: PostCardNavigation,
    navigateToSettings: () -> Unit,
) {
    composable(
        route = NavigationDestination.Account.fullRoute,
        arguments = listOf(
            navArgument(NavigationDestination.Account.NAV_PARAM_ACCOUNT_ID) {
                nullable = true
            }
        )
    ) {
        val accountId: String? = it.arguments?.getString(NavigationDestination.Account.NAV_PARAM_ACCOUNT_ID)
        AccountScreen(
            accountId = accountId,
            accountNavigationCallbacks = accountNavigationCallbacks,
            postCardNavigation = postCardNavigation,
            navigateToSettings = navigateToSettings,
        )
    }
}
