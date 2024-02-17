package social.firefly.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import social.firefly.common.utils.mosoFadeIn
import social.firefly.common.utils.mosoFadeOut
import social.firefly.core.navigation.BottomBarNavigationDestination
import social.firefly.feature.account.myAccountScreen
import social.firefly.feature.discover.discoverScreen
import social.firefly.feature.notifications.notificationsScreen
import social.firefly.feed.feedScreen

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
