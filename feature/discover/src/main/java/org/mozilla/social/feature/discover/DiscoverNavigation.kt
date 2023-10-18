package org.mozilla.social.feature.discover

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mozilla.social.core.navigation.NavigationDestination

fun NavController.navigateToDiscover(
    navOptions: NavOptions? = null,
) {
    navigate(NavigationDestination.Discover.route, navOptions)
}

fun NavGraphBuilder.discoverScreen(
) {

}