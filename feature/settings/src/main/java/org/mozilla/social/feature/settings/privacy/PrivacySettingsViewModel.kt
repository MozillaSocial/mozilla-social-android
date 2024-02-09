package org.mozilla.social.feature.settings.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.analytics.SettingsAnalytics

class PrivacySettingsViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val analytics: SettingsAnalytics,
) : ViewModel(), PrivacySettingsInteractions {

    val allowAnalytics =
        appPreferencesDatastore.allowAnalytics.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun toggleAllowAnalytics() {
        val toggleAnalyticsValue = allowAnalytics.value.not()
        analytics.collectDataToggled(toggleAnalyticsValue)
        viewModelScope.launch {
            appPreferencesDatastore.allowAnalytics(toggleAnalyticsValue)
        }
    }

    override fun onScreenViewed() {
        analytics.privacySettingsViewed()
    }
}
