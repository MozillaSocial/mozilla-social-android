package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo

class SettingsViewModel(
    private val analytics: Analytics,
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val navigateTo: NavigateTo,
) : ViewModel(), SettingsInteractions {

    private val _isAnalyticsToggledOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isAnalyticsToggledOn = _isAnalyticsToggledOn.asStateFlow()

    init {
        viewModelScope.launch {
            appPreferencesDatastore.allowAnalytics.collectLatest { enabled ->
                _isAnalyticsToggledOn.value = enabled
            }
        }
    }

    fun onAboutClicked() {
        navigateTo(SettingsNavigationDestination.AboutSettings)
    }

    fun onAccountClicked() {
        navigateTo(SettingsNavigationDestination.AccountSettings)
    }

    fun onContentPreferencesClicked() {
        navigateTo(SettingsNavigationDestination.ContentPreferencesSettings)
    }

    fun onPrivacyClicked() {
        navigateTo(SettingsNavigationDestination.PrivacySettings)
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_SCREEN_IMPRESSION
        )
    }
}
