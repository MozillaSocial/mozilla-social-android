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
            },
    ),
    DISCOVER(
        bottomBarTab =
            object : BottomBarTab {
                @Composable
                override fun selectedIcon(): Painter {
                    return MoSoIcons.compassFill()
                }

                @Composable
                override fun unselectedIcon(): Painter {
                    return MoSoIcons.compass()
                }

                override val tabText = StringFactory.resource(R.string.discover_tab_text)
                override val navigationDestination: Destination =
                    Destination.BottomBar(BottomBarNavigationDestination.Discover)
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
            },
    ),
}

object Routes {
    const val SPLASH = "splash"
}
