package social.firefly.core.navigation

import androidx.navigation.NavController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import social.firefly.core.navigation.SettingsNavigationDestination.AboutSettings.navigateToAboutSettings
import social.firefly.core.navigation.SettingsNavigationDestination.AccountSettings.navigateToAccountSettings
import social.firefly.core.navigation.SettingsNavigationDestination.MainSettings.navigateToMainSettings
import social.firefly.core.navigation.SettingsNavigationDestination.PrivacySettings.navigateToPrivacySettings

class SettingsNavigationTest {
    private val navController = mockk<NavController>(relaxed = true)

    @Test
    fun navigateToMainSettings() {
        navController.navigateToMainSettings()

        verify {
            navController.navigate("mainSettings")
        }
    }

    @Test
    fun navigateToAccountSettings() {
        navController.navigateToAccountSettings()

        verify {
            navController.navigate("accountSettings")
        }
    }

    @Test
    fun navigateToPrivacySettings() {
        navController.navigateToPrivacySettings()

        verify {
            navController.navigate("privacySettings")
        }
    }

    @Test
    fun navigateToAboutSettings() {
        navController.navigateToAboutSettings()

        verify {
            navController.navigate("aboutSettings")
        }
    }
}
