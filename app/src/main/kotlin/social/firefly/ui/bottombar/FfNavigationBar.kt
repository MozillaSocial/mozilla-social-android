package social.firefly.ui.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.navigation.BottomBarNavigationDestination
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.ui.common.FfSurface
import social.firefly.ui.bottombar.Destination.Main

@Composable
fun FfBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: BottomBarNavigationDestination,
    bottomBarTabs: List<BottomBarTab>,
    navigateTo: (route: Destination) -> Unit,
    containerColor: Color = FfNavigationBarDefaults.containerColor,
    contentColor: Color = FfTheme.colors.iconPrimary,
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = FfNavigationBarDefaults.windowInsets,
) {
    val context = LocalContext.current
    FfNavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        windowInsets = windowInsets,
    ) {
        bottomBarTabs.forEach {
            when (it.navigationDestination) {
                is Destination.BottomBar -> {
                    FfNavigationBarItem(
                        modifier = Modifier
                            .semantics { contentDescription = it.tabText.build(context) },
                        destination = it,
                        isSelected =
                            currentDestination ==
                                (it.navigationDestination as? Destination.BottomBar)?.bottomBarNavigationDestination,
                        navigateTo = navigateTo,
                    )
                }

                is Main -> {
                    NavigationBarItem(
                        modifier = Modifier
                            .semantics { contentDescription = it.tabText.build(context) }
                            .height(48.dp),
                        selected = false,
                        onClick = { navigateTo(it.navigationDestination) },
                        colors = FfNavigationBarItemDefaults.colors(),
                        icon = {
                            Icon(
                                painter = FfIcons.connect(),
                                modifier =
                                    Modifier
                                        .size(40.dp),
                                contentDescription = null,
                                tint = FfTheme.colors.actionPrimary,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun FfNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = FfNavigationBarDefaults.containerColor,
    contentColor: Color = FfTheme.colors.iconPrimary,
    tonalElevation: Dp = FfNavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = FfNavigationBarDefaults.windowInsets,
    height: Dp = FfNavigationBarDefaults.height,
    content: @Composable RowScope.() -> Unit,
) {
    FfSurface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier,
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .height(height)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            content = content,
        )
    }
}

@Composable
private fun RowScope.FfNavigationBarItem(
    modifier: Modifier = Modifier,
    destination: BottomBarTab,
    isSelected: Boolean,
    navigateTo: (route: Destination) -> Unit,
) {
    NavigationBarItem(
        modifier = modifier
            .height(48.dp),
        selected = isSelected,
        onClick = {
            if (isSelected) {
                destination.doubleTapDestination?.let { destination -> navigateTo(destination) }
            } else {
                navigateTo(destination.navigationDestination)
            }
        },
        colors = FfNavigationBarItemDefaults.colors(),
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
            modifier =
                Modifier
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
    val navigationDestination: Destination
    // Destination for when the user taps the icon a second time
    val doubleTapDestination: Destination?
}

/**
 * The type of destination the bottom bar tab goes to. If it's [Main], it navigates to a
 * non-bottom-tab destination
 */
sealed interface Destination {
    data class BottomBar(val bottomBarNavigationDestination: BottomBarNavigationDestination) :
        Destination

    data class Main(val navigationDestination: NavigationDestination) : Destination
}

object FfNavigationDefaults {
    @Composable
    fun unselectedItemColor() = FfTheme.colors.iconPrimary

    @Composable
    fun selectedItemColor() = FfTheme.colors.actionPrimary

    @Composable
    fun indicatorColor() = FfTheme.colors.layer1
}

object FfNavigationBarItemDefaults {
    @Composable
    fun colors(): NavigationBarItemColors {
        return NavigationBarItemDefaults.colors(
            selectedIconColor = FfNavigationDefaults.selectedItemColor(),
            unselectedIconColor = FfNavigationDefaults.unselectedItemColor(),
            selectedTextColor = FfNavigationDefaults.selectedItemColor(),
            unselectedTextColor = FfNavigationDefaults.unselectedItemColor(),
            indicatorColor = FfNavigationDefaults.indicatorColor(),
        )
    }
}

object FfNavigationBarDefaults {
    /** Default elevation for a navigation bar. */
    val Elevation: Dp = 0.dp

    /** Default color for a navigation bar. */
    val containerColor: Color @Composable get() = FfTheme.colors.layer1

    /**
     * Default window insets to be used and consumed by navigation bar
     */
    val windowInsets: WindowInsets
        @Composable
        get() =
            WindowInsets.systemBars
                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)

    val height: Dp
        get() = 48.dp

    fun Modifier.bottomBarPadding(currentDestination: NavigationDestination?): Modifier =
        if (currentDestination == NavigationDestination.Tabs) {
            padding(bottom = height)
        } else {
            this
        }
}
