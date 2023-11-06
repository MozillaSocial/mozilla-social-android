package org.mozilla.social.feature.settings

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class SettingsViewModelTest {

    private lateinit var objUnderTest: SettingsViewModel

    private val navigateTo: NavigateTo = mockk(relaxed = true)

    @Before
    fun setup() {
        objUnderTest = SettingsViewModel(navigateTo)
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