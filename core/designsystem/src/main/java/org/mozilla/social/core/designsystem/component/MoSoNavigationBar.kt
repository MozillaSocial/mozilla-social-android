package org.mozilla.social.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun MoSoBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavBarDestination,
    navBarDestinations: List<NavBarDestination>,
    navigateTo: (NavBarDestination) -> Unit,
    containerColor: Color = MoSoNavigationBarDefaults.containerColor,
    contentColor: Color = MoSoTheme.colors.iconPrimary,
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = MoSoNavigationBarDefaults.windowInsets,
) {
    MoSoNavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        windowInsets = windowInsets
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
fun MoSoNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = MoSoNavigationBarDefaults.containerColor,
    contentColor: Color = MoSoTheme.colors.iconPrimary,
    tonalElevation: Dp = MoSoNavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = MoSoNavigationBarDefaults.windowInsets,
    height: Dp = MoSoNavigationBarDefaults.height,
    content: @Composable RowScope.() -> Unit
) {
    MoSoSurface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .height(height)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            content = content
        )
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
        modifier = modifier.height(48.dp),
        selected = isSelected,
        onClick = { navigateTo(destination) },
        colors = MoSoNavigationBarItemDefaults.colors(),
        icon = {
            MoSoIcon(
                destination = destination, isSelected = isSelected
            )
        },
//        label = {
//            Text(
//                text = destination.tabText.build(
//                    LocalContext.current,
//                ),
//                style = Typography.bodyLarge,
//            )
//        }
    )
}

@Composable
private fun MoSoIcon(
    destination: NavBarDestination, isSelected: Boolean
) {
    if (isSelected) {
//        val a = destination.selectedIcon
//        val b = a.build()
        Icon(
            painter = destination.selectedIcon(),
            modifier = Modifier
                .size(24.dp),
            contentDescription = null,
        )
    } else {
        Icon(
            painter = destination.selectedIcon(),
            modifier = Modifier.size(24.dp),
            contentDescription = null,
        )

//        Icon(
//            imageVector = destination.unselectedIcon,
//            contentDescription = null,
//        )
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
    @Composable
    fun selectedIcon(): Painter
    val tabText: StringFactory
}

object MoSoNavigationDefaults {

    @Composable
    fun unselectedItemColor() = MoSoTheme.colors.iconPrimary

    @Composable
    fun selectedItemColor() = MoSoTheme.colors.actionPrimary

    @Composable
    fun indicatorColor() = MoSoTheme.colors.layer1

}

object MoSoNavigationBarItemDefaults {
    @Composable
    fun colors(): NavigationBarItemColors {
        return NavigationBarItemDefaults.colors(
            selectedIconColor = MoSoNavigationDefaults.selectedItemColor(),
            unselectedIconColor = MoSoNavigationDefaults.unselectedItemColor(),
            selectedTextColor = MoSoNavigationDefaults.selectedItemColor(),
            unselectedTextColor = MoSoNavigationDefaults.unselectedItemColor(),
            indicatorColor = MoSoNavigationDefaults.indicatorColor(),
        )
    }
}

object MoSoNavigationBarDefaults {
    /** Default elevation for a navigation bar. */
    val Elevation: Dp = 0.dp

    /** Default color for a navigation bar. */
    val containerColor: Color @Composable get() = MoSoTheme.colors.layer1

    /**
     * Default window insets to be used and consumed by navigation bar
     */
    val windowInsets: WindowInsets
        @Composable
        get() = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)

    val height: Dp
        get() = 48.dp
}