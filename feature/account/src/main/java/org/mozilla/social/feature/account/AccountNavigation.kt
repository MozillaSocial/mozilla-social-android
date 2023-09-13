package org.mozilla.social.feature.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


/**
 * The my account route is used for the account tab.  The account route is used for any
 * account that will open outside of the bottom navigation tab.
 */
const val MY_ACCOUNT_ROUTE = "myAccount"

const val ACCOUNT_ROUTE = "account"
const val ACCOUNT_ID = "accountId"
const val ACCOUNT_FULL_ROUTE = "$ACCOUNT_ROUTE?$ACCOUNT_ID={$ACCOUNT_ID}"

fun NavController.navigateToAccount(
    navOptions: NavOptions? = null,
    accountId: String? = null,
) {
    println("johnny accountId: $accountId")
    when {
        accountId != null -> navigate("$ACCOUNT_ROUTE?$ACCOUNT_ID=$accountId", navOptions)
        else -> navigate(MY_ACCOUNT_ROUTE, navOptions)
    }
}

fun NavGraphBuilder.accountScreen(
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onLoggedOut: () -> Unit
) {

    composable(
        route = MY_ACCOUNT_ROUTE,
    ) {
        println("johnny a1")
        AccountRoute(
            accountId = null,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            onLoggedOut = onLoggedOut
        )
    }

    composable(
        route = ACCOUNT_FULL_ROUTE,
        arguments = listOf(
            navArgument(ACCOUNT_ID) {
                nullable = true
            }
        )
    ) {
        println("johnny a2")
        val accountId: String? = it.arguments?.getString(ACCOUNT_ID)
        AccountRoute(
            accountId = accountId,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            onLoggedOut = onLoggedOut
        )
    }
}
