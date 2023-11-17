package org.mozilla.social.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.mozilla.social.common.utils.mosoFadeIn
import org.mozilla.social.common.utils.mosoFadeOut
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.navigation.BottomBarNavigationDestination
import org.mozilla.social.feature.account.myAccountScreen
import org.mozilla.social.feature.discover.discoverScreen
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
        bookmarkScreen()
    }
}

fun NavGraphBuilder.bookmarkScreen() {
    composable(route = BottomBarNavigationDestination.Bookmarks.route) {
        MoSoSurface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "bookmarks coming soon :)"
                )
            }
        }
    }
}