package org.mozilla.social.core.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MoSoNavigationBar(
    currentDestination: NavBarDestination,
    navBarDestinations: List<NavBarDestination>,
    navigateTo: (NavBarDestination) -> Unit
) {
    NavigationBar {
        navBarDestinations.forEach { destination ->
            val isSelected = currentDestination == destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { navigateTo(destination) },
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
                label = { Text(text = destination.tabText) }
            )
        }
    }
}


/**
 * Corresponds with the navigation destinations in the main (logged in) graph
 */
interface NavDestination {
    val route: String
}

/**
 * The navigation destinations which correspond to the bottom navigation tabs
 */
interface NavBarDestination: NavDestination {
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val tabText: String
}