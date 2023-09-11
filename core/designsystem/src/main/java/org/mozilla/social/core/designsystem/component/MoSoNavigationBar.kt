package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.StringFactory

@Composable
fun MoSoBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavBarDestination,
    navBarDestinations: List<NavBarDestination>,
    navigateTo: (NavBarDestination) -> Unit
) {

    NavigationBar(
        modifier = modifier,
        contentColor = MoSoNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp
    ) {
        navBarDestinations.forEach { navBarDestination ->
            MoSoNavigationBarItem(
                destination = navBarDestination,
                isSelected = currentDestination == navBarDestination,
                navigateTo = navigateTo,
            )
        }
    }
}

@Composable
fun RowScope.MoSoNavigationBarItem(
    modifier: Modifier = Modifier,
    destination: NavBarDestination,
    isSelected: Boolean,
    navigateTo: (NavBarDestination) -> Unit
) {
    NavigationBarItem(
        modifier = modifier,
        selected = isSelected,
        onClick = { navigateTo(destination) },
        colors = MoSoNavigationBarItemDefaults.colors(),
        icon = { MoSoIcon(destination = destination, isSelected = isSelected) },
        label = { Text(text = destination.tabText.build(LocalContext.current)) }
    )
}

@Composable
private fun MoSoIcon(destination: NavBarDestination, isSelected: Boolean) {
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
interface NavBarDestination : NavDestination {
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val tabText: StringFactory
}

object MoSoNavigationDefaults {

    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer

}

object MoSoNavigationBarItemDefaults {
    @Composable
    fun colors(): NavigationBarItemColors {
        return NavigationBarItemDefaults.colors(
            selectedIconColor = MoSoNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = MoSoNavigationDefaults.navigationContentColor(),
            selectedTextColor = MoSoNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = MoSoNavigationDefaults.navigationContentColor(),
            indicatorColor = MoSoNavigationDefaults.navigationIndicatorColor(),
        )
    }
}