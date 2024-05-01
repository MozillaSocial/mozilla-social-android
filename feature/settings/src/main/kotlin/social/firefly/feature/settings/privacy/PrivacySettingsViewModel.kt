package social.firefly.feature.settings.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.datastore.AppPreferencesDatastore

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
