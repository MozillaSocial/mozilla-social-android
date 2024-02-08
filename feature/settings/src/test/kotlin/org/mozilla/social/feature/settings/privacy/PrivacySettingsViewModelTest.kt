package org.mozilla.social.feature.settings.privacy

import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.analytics.SettingsAnalytics

class PrivacySettingsViewModelTest {
    private lateinit var objUnderTest: PrivacySettingsViewModel

    private val appPreferencesDataStore: AppPreferencesDatastore = mockk(relaxed = true)
    private val allowAnalytics = MutableSharedFlow<Boolean>(replay = 1)
    private val analytics: SettingsAnalytics = mockk(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { appPreferencesDataStore.allowAnalytics } returns allowAnalytics
        objUnderTest = PrivacySettingsViewModel(
            appPreferencesDataStore,
            analytics,
        )
    }

    @Test
    fun allowAnalyticsCorrectInitialValueTrue() {
        every { appPreferencesDataStore.allowAnalytics } returns allowAnalytics
        assertThat(objUnderTest.allowAnalytics.value).isEqualTo(true)
    }

    @Test
    fun allowAnalyticsIsDataStoreValue() {
        allowAnalytics.tryEmit(false)
        assertThat(objUnderTest.allowAnalytics.value).isEqualTo(false)
    }

    @Test
    fun toggleAllowAnalyticsOff() {
        allowAnalytics.tryEmit(true)
        objUnderTest.toggleAllowAnalytics()
        coVerify { appPreferencesDataStore.allowAnalytics(false) }
    }

    @Test
    fun toggleAllowAnalyticsOn() {
        allowAnalytics.tryEmit(false)
        objUnderTest.toggleAllowAnalytics()
        coVerify { appPreferencesDataStore.allowAnalytics(true) }
    }
}
