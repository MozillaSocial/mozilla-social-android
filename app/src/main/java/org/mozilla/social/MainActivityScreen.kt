package org.mozilla.social

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.authScreen

@Composable
fun MainActivityScreen() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = AUTH_ROUTE) {
        authScreen(
            onSignedIn = {
                //TODO something is broken with the backstack due to custom chrome tabs I think
                navController.navigate(
                    "timeline",
                    navOptions = NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(AUTH_ROUTE, true)
                        .build()
                )
            }
        )
        composable("timeline") { TimelineScreen() }
    }
}

@Composable
fun TimelineScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "TimelineScreen")
    }
}
