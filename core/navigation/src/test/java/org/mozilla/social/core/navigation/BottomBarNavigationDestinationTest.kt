package org.mozilla.social.core.navigation

import androidx.navigation.NavController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mozilla.social.core.navigation.BottomBarNavigationDestination.Discover.navigateToDiscover
import org.mozilla.social.core.navigation.BottomBarNavigationDestination.Feed.navigateToFeed
import org.mozilla.social.core.navigation.BottomBarNavigationDestination.MyAccount.navigateToMyAccount
import org.mozilla.social.core.navigation.BottomBarNavigationDestination.Notifications.navigateToNotificationsScreen

class BottomBarNavigationDestinationTest {
    private val navController = mockk<NavController>(relaxed = true)

    @Test
    fun navigateToMyAccount() {
        navController.navigateToMyAccount()

        verify { navController.navigate("myAccount") }
    }

    @Test
    fun navigateToDiscover() {
        navController.navigateToDiscover()

        verify { navController.navigate("discover") }
    }

    @Test
    fun navigateToFeed() {
        navController.navigateToFeed()

        verify { navController.navigate("feed") }
    }

    @Test
    fun navigateToNotifications() {
        navController.navigateToNotificationsScreen()

        verify { navController.navigate("notifications") }
    }
}
