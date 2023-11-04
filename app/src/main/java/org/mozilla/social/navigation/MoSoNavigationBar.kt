package org.mozilla.social.navigation

import org.mozilla.social.core.designsystem.component.MoSoSurface

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.NavigationDestination

@Composable
fun MoSoBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavigationDestination,
    bottomBarTabs: List<BottomBarTab>,
    navigateTo: (route: NavigationDestination) -> Unit,
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
        bottomBarTabs.forEach {
            if (it == BottomBarTabs.NEW_POST.bottomBarTab) {
                NavigationBarItem(
                    modifier = modifier.height(48.dp),
                    selected = false,
                    onClick = { navigateTo(NavigationDestination.NewPost) },
                    colors = MoSoNavigationBarItemDefaults.colors(),
                    icon = {
                        Icon(
                            painter = MoSoIcons.connect(),
                            modifier = Modifier
                                .size(40.dp),
                            contentDescription = null,
                            tint = MoSoTheme.colors.actionPrimary,
                        )
                    },
                )
            } else {
                MoSoNavigationBarItem(
                    destination = it,
                    isSelected = currentDestination == it.navigationDestination,
                    navigateTo = navigateTo,
                )
            }

        }
    }
}

@Composable
private fun MoSoNavigationBar(
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
private fun RowScope.MoSoNavigationBarItem(
    modifier: Modifier = Modifier,
    destination: BottomBarTab,
    isSelected: Boolean,
    navigateTo: (route: NavigationDestination) -> Unit
) {
    NavigationBarItem(
        modifier = modifier.height(48.dp),
        selected = isSelected,
        onClick = { navigateTo(destination.navigationDestination) },
        colors = MoSoNavigationBarItemDefaults.colors(),
        icon = {
            BottomBarIcon(
                destination = destination,
                isSelected = isSelected,
            )
        },
    )
}

@Composable
private fun BottomBarIcon(
    destination: BottomBarTab,
    isSelected: Boolean,
) {
    if (isSelected) {
        Icon(
            painter = destination.selectedIcon(),
            modifier = Modifier
                .size(24.dp),
            contentDescription = null,
        )
    } else {
        Icon(
            painter = destination.unselectedIcon(),
            modifier = Modifier.size(24.dp),
            contentDescription = null,
        )
    }
}

/**
 * The navigation destinations which correspond to the bottom navigation tabs
 */
interface BottomBarTab {
    @Composable
    fun selectedIcon(): Painter
    @Composable
    fun unselectedIcon(): Painter
    val tabText: StringFactory
    val navigationDestination: NavigationDestination
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