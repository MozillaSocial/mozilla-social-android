package social.firefly.feature.settings

import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Test
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import kotlin.test.BeforeTest

class SettingsViewModelTest {
    private lateinit var objUnderTest: SettingsViewModel

    private val navigateTo: NavigateTo = mockk(relaxed = true)
    private val analytics: SettingsAnalytics = mockk(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        objUnderTest =
            SettingsViewModel(
                analytics = analytics,
                navigateTo = navigateTo,
            )
    }

    @Test
    fun onAboutClicked() {
        objUnderTest.onAboutClicked()

        verify { navigateTo(SettingsNavigationDestination.AboutSettings) }
    }

    @Test
    fun onAccountClicked() {
        objUnderTest.onAccountClicked()

        verify { navigateTo(SettingsNavigationDestination.AccountSettings) }
    }

    @Test
    fun onPrivacyClicked() {
        objUnderTest.onPrivacyClicked()

        verify { navigateTo(SettingsNavigationDestination.PrivacySettings) }
    }
}
