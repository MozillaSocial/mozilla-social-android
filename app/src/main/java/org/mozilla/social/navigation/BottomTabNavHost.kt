package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.mozilla.social.common.utils.mosoFadeIn
import org.mozilla.social.common.utils.mosoFadeOut
import org.mozilla.social.core.navigation.BottomBarNavigationDestination
import org.mozilla.social.feature.account.myAccountScreen
import org.mozilla.social.feature.discover.discoverScreen
import org.mozilla.social.feature.notifications.notificationsScreen
import org.mozilla.social.feed.feedScreen

@Composable
fun BottomTabNavHost(
    modifier: Modifier = Modifier,
    tabbedNavController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = tabbedNavController,
        startDestination = BottomBarNavigationDestination.Feed.route,
        enterTransition = { mosoFadeIn() },
        exitTransition = { mosoFadeOut() },
        popEnterTransition = { mosoFadeIn() },
        popExitTransition = { mosoFadeOut() },
    ) {
        feedScreen()
        discoverScreen()
        myAccountScreen()
        notificationsScreen()
    }
}
