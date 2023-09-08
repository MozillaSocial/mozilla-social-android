package org.mozilla.social.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import org.mozilla.social.core.designsystem.component.NavBarDestination
import org.mozilla.social.core.designsystem.component.NavDestination
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.feature.account.ACCOUNT_ROUTE
import org.mozilla.social.feature.settings.SETTINGS_ROUTE
import org.mozilla.social.feed.FEED_ROUTE
import org.mozilla.social.post.NEW_POST_ROUTE
import org.mozilla.social.search.SEARCH_ROUTE

object Feed : NavBarDestination {
    override val selectedIcon = MoSoIcons.Home
    override val unselectedIcon = Icons.Outlined.Home
    override val tabText = "Feed"
    override val route = FEED_ROUTE
}

object Search : NavBarDestination {
    override val selectedIcon = MoSoIcons.Search
    override val unselectedIcon = Icons.Outlined.Search
    override val tabText = "Search"
    override val route = SEARCH_ROUTE
}

object Settings : NavBarDestination {
    override val route: String = SETTINGS_ROUTE
    override val selectedIcon = MoSoIcons.Settings
    override val unselectedIcon = Icons.Outlined.Settings
    override val tabText = "Settings"
}

object Account : NavBarDestination {
    override val route: String = ACCOUNT_ROUTE
    override val selectedIcon = MoSoIcons.AccountCircle
    override val unselectedIcon = Icons.Outlined.AccountCircle
    override val tabText = "Account"
}

object NewPost : NavDestination {
    override val route: String = NEW_POST_ROUTE
}