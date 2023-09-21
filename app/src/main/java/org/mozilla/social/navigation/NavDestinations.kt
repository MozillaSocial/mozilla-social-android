package org.mozilla.social.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.mozilla.social.R
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.component.NavBarDestination
import org.mozilla.social.core.designsystem.component.NavDestination
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.post.NEW_POST_ROUTE

object Feed : NavBarDestination {
    @Composable
    override fun selectedIcon(): Painter {
        return MoSoIcons.house()
    }

    override val tabText = StringFactory.resource(R.string.feed_tab_text)
    override val route = Routes.FEED
}

object Discover : NavBarDestination {
    @Composable
    override fun selectedIcon(): Painter {
        return MoSoIcons.compass()
    }

    override val tabText = StringFactory.resource(R.string.search_tab_text)
    override val route = Routes.DISCOVER
}

object Bookmarks : NavBarDestination {
    override val route: String = Routes.BOOKMARKS

    @Composable
    override fun selectedIcon(): Painter {
        return MoSoIcons.bookmark()
    }

    override val tabText = StringFactory.resource(R.string.settings_tab_text)
}


object Account : NavBarDestination {
    override val route: String = Routes.MY_ACCOUNT

    @Composable
    override fun selectedIcon(): Painter {
        return MoSoIcons.userCircle()
    }

    override val tabText = StringFactory.resource(R.string.account_tab_text)
}

object NewPost : NavDestination {
    override val route: String = NEW_POST_ROUTE
}

object Routes {
    const val AUTH = "auth"
    const val SPLASH = "splash"
    const val BOOKMARKS = "bookmarks"
    const val DISCOVER = "discover"
    const val MAIN = "main"
    const val FEED = "feed"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val MY_ACCOUNT = "myAccount"
}