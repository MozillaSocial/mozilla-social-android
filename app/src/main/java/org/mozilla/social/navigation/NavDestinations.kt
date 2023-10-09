package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.mozilla.social.R
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.NavBarDestination
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.feature.account.MY_ACCOUNT_ROUTE
import org.mozilla.social.feed.FEED_ROUTE

import org.mozilla.social.search.SEARCH_ROUTE

/**
 * An enum of all the bottom nav bar destinations
 *
 * Using an enum instead of a sealed class so we can iterate through all the values
 * without requiring the kotlin reflect dependency
 */
enum class NavBarDestinations(
    val navBarDestination: NavBarDestination,
) {
    FEED(
        navBarDestination = object : NavBarDestination {
            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.house()
            }

            override val tabText = StringFactory.resource(R.string.feed_tab_text)
            override val route = FEED_ROUTE
        }
    ),
    DISCOVER(
        navBarDestination = object : NavBarDestination {
            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.compass()
            }

            override val tabText = StringFactory.resource(R.string.search_tab_text)
            override val route = SEARCH_ROUTE
        }
    ),
    BOOKMARKS(
        navBarDestination = object : NavBarDestination {
            override val route: String = Routes.BOOKMARKS

            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.bookmark()
            }

            override val tabText = StringFactory.resource(R.string.settings_tab_text)
        }
    ),
    ACCOUNT(
        navBarDestination = object : NavBarDestination {
            override val route: String = MY_ACCOUNT_ROUTE

            @Composable
            override fun selectedIcon(): Painter {
                return MoSoIcons.userCircle()
            }

            override val tabText = StringFactory.resource(R.string.account_tab_text)

        }
    )
}

object Routes {
    const val SPLASH = "splash"
    const val BOOKMARKS = "bookmarks"
    const val MAIN = "main"
}