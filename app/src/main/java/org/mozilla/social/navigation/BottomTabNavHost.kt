package org.mozilla.social.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.mosoFadeIn
import org.mozilla.social.common.utils.mosoFadeOut
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.navigation.NavigationDestination
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
        startDestination = NavigationDestination.Feed.route,
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
    composable(route = NavigationDestination.Bookmarks.route) {
        MoSoSurface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "bookmarks coming soon :)"
                )
            }
        }
    }
}