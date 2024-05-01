package social.firefly.core.navigation

import androidx.navigation.NavController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import social.firefly.core.navigation.BottomBarNavigationDestination.Discover.navigateToDiscover
import social.firefly.core.navigation.BottomBarNavigationDestination.Feed.navigateToFeed
import social.firefly.core.navigation.BottomBarNavigationDestination.MyAccount.navigateToMyAccount
import social.firefly.core.navigation.BottomBarNavigationDestination.Notifications.navigateToNotificationsScreen

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
