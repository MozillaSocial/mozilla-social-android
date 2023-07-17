package org.mozilla.social.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.navigation.MozillaNavHost
import org.mozilla.social.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen() {
    val navController: NavHostController = rememberNavController()

    val appState = rememberAppState(navController = navController)
    Scaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                BottomBar(
                    currentDestination = appState.currentTopLevelDestination,
                    onNavigationToDestination = appState::navigateToTopLevelDestination
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            MozillaNavHost(navController = navController)
        }
    }
}

@Composable
private fun BottomBar(
    currentDestination: TopLevelDestination?,
    onNavigationToDestination: (TopLevelDestination) -> Unit,
) {
    NavigationBar {
        AppState.topLevelDestinations.forEach { destination ->
            val isSelected = currentDestination == destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigationToDestination(destination) },
                icon = {
                    if (isSelected) {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = null,
                        )
                    }
                },
                label = { Text(text = destination.text) }
            )
        }
    }
}

@Preview
@Composable
private fun DashboardPreview() {
    MozillaSocialTheme {
        BottomBar(
            TopLevelDestination.FEED
        ) {}
    }
}