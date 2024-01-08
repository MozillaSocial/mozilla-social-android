package org.mozilla.social.feature.notifications

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.mozilla.social.core.navigation.BottomBarNavigationDestination

fun NavGraphBuilder.notificationsScreen() {
    composable(route = BottomBarNavigationDestination.Notifications.route) {
        NotificationsScreen()
    }
}