package social.firefly.feature.notifications

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import social.firefly.core.navigation.BottomBarNavigationDestination

fun NavGraphBuilder.notificationsScreen() {
    composable(route = BottomBarNavigationDestination.Notifications.route) {
        NotificationsScreen()
    }
}