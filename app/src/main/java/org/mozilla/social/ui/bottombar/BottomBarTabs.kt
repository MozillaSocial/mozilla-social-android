package org.mozilla.social.ui.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.mozilla.social.R
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.navigation.BottomBarNavigationDestination
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
        bottomBarTab =
            object : BottomBarTab {
                @Composable
                override fun selectedIcon(): Painter {
                    return MoSoIcons.houseFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return MoSoIcons.house()
                }

                override val tabText = StringFactory.resource(R.string.feed_tab_text)
                override val navigationDestination: Destination =
                    Destination.BottomBar(BottomBarNavigationDestination.Feed)
                override val doubleTapDestination: Destination? = null
            },
    ),
    DISCOVER(
        bottomBarTab =
            object : BottomBarTab {
                @Composable
                override fun selectedIcon(): Painter {
                    return MoSoIcons.magnifyingGlassFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return MoSoIcons.magnifyingGlass()
                }

                override val tabText = StringFactory.resource(R.string.discover_tab_text)
                override val navigationDestination: Destination =
                    Destination.BottomBar(BottomBarNavigationDestination.Discover)
                override val doubleTapDestination: Destination =
                    Destination.Main(NavigationDestination.Search)
            },
    ),
    NEW_POST(
        bottomBarTab =
            object : BottomBarTab {
                @Composable
                override fun selectedIcon(): Painter {
                    return MoSoIcons.connect()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return MoSoIcons.connect()
                }

                override val tabText = StringFactory.resource(R.string.new_post_tab_text)
                override val navigationDestination: Destination =
                    Destination.Main(NavigationDestination.NewPost())
                override val doubleTapDestination: Destination? = null
            },
    ),
    NOTIFICATIONS(
        bottomBarTab =
            object : BottomBarTab {
                @Composable
                override fun selectedIcon(): Painter {
                    return MoSoIcons.bellFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return MoSoIcons.bell()
                }

                override val tabText = StringFactory.resource(R.string.notifications_tab_text)
                override val navigationDestination: Destination =
                    Destination.BottomBar(BottomBarNavigationDestination.Notifications)
                override val doubleTapDestination: Destination? = null
            },
    ),
    ACCOUNT(
        bottomBarTab =
            object : BottomBarTab {
                @Composable
                override fun selectedIcon(): Painter {
                    return MoSoIcons.userCircleFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return MoSoIcons.userCircle()
                }

                override val tabText = StringFactory.resource(R.string.account_tab_text)
                override val navigationDestination: Destination =
                    Destination.BottomBar(BottomBarNavigationDestination.MyAccount)
                override val doubleTapDestination: Destination? = null
            },
    ),
}

object Routes {
    const val SPLASH = "splash"
}
