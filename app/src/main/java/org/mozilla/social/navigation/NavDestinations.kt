package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.mozilla.social.R
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.navigation.NavigationDestination

/**
 * An enum of all the bottom nav bar destinations
 *
 * Using an enum instead of a sealed class so we can iterate through all the values
 * without requiring the kotlin reflect dependency
 */
enum class BottomBarTabs(
    val bottomBarTab: BottomBarTab,
) {
    FEED(
        bottomBarTab = object : BottomBarTab {
            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.house()
            }

            override val tabText = StringFactory.resource(R.string.feed_tab_text)
            override val navigationDestination: NavigationDestination = NavigationDestination.Feed
        }
    ),
    DISCOVER(
        bottomBarTab = object : BottomBarTab {
            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.compass()
            }

            override val tabText = StringFactory.resource(R.string.discover_tab_text)
            override val navigationDestination: NavigationDestination = NavigationDestination.Discover
        }
    ),
    BOOKMARKS(
        bottomBarTab = object : BottomBarTab {
            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.bookmark()
            }

            override val tabText = StringFactory.resource(R.string.settings_tab_text)
            override val navigationDestination: NavigationDestination = NavigationDestination.Bookmarks
        }
    ),
    ACCOUNT(
        bottomBarTab = object : BottomBarTab {
            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.userCircle()
            }

            override val tabText = StringFactory.resource(R.string.account_tab_text)
            override val navigationDestination: NavigationDestination = NavigationDestination.MyAccount

        }
    )
}

object Routes {
    const val SPLASH = "splash"
    const val BOOKMARKS = "bookmarks"
    const val MAIN = "main"
}