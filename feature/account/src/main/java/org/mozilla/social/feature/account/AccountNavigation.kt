package org.mozilla.social.feature.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.mozilla.social.core.ui.postcard.PostCardNavigation


/**
 * The my account route is used for the account tab.  The account route is used for any
 * account that will open outside of the bottom navigation tab.
 */
private const val MY_ACCOUNT_ROUTE = "myAccount"

private const val ACCOUNT_ROUTE = "account"
private const val ACCOUNT_ID = "accountId"
private const val ACCOUNT_FULL_ROUTE = "$ACCOUNT_ROUTE?$ACCOUNT_ID={$ACCOUNT_ID}"

fun NavController.navigateToAccount(
    navOptions: NavOptions? = null,
    accountId: String? = null,
) {
    when {
        accountId != null -> navigate("$ACCOUNT_ROUTE?$ACCOUNT_ID=$accountId", navOptions)
        else -> navigate(MY_ACCOUNT_ROUTE, navOptions)
    }
}

// The custom screen here doesn't seem to be used anywhere, so I'm going to 
fun NavGraphBuilder.accountScreen(
    onFollowingClicked: () -> Unit,
    onFollowersClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    postCardNavigation: PostCardNavigation,
) {

    composable(
        route = MY_ACCOUNT_ROUTE,
    ) {
        AccountScreen(
            accountId = null,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            postCardNavigation = postCardNavigation,
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
        val accountId: String? = it.arguments?.getString(ACCOUNT_ID)
        AccountScreen(
            accountId = accountId,
            onFollowingClicked = onFollowingClicked,
            onFollowersClicked = onFollowersClicked,
            onCloseClicked = onCloseClicked,
            postCardNavigation = postCardNavigation,
        )
    }
}
