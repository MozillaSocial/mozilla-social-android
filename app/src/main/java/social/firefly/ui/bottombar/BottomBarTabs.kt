package social.firefly.ui.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import social.firefly.R
import social.firefly.common.utils.StringFactory
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.navigation.BottomBarNavigationDestination
import social.firefly.core.navigation.NavigationDestination

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
                    return FfIcons.houseFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return FfIcons.house()
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
                    return FfIcons.magnifyingGlassFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return FfIcons.magnifyingGlass()
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
                    return FfIcons.connect()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return FfIcons.connect()
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
                    return FfIcons.bellFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return FfIcons.bell()
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
                    return FfIcons.userCircleFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return FfIcons.userCircle()
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
