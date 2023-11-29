package org.mozilla.social.feature.settings.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.analytics.EngagementType
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.feature.settings.SettingsInteractions

class PrivacySettingsViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    val allowAnalytics =
        appPreferencesDatastore.allowAnalytics.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun toggleAllowAnalytics() {
        val toggleAnalyticsValue = allowAnalytics.value.not()
        analytics.uiEngagement(
            engagementType = EngagementType.GENERAL,
            uiIdentifier = AnalyticsIdentifiers.PRIVACY_COLLECT_DATA_TOGGLE,
            engagementValue = toggleAnalyticsValue.toString(),
        )
        viewModelScope.launch {
            appPreferencesDatastore.allowAnalytics(toggleAnalyticsValue)
        }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_PRIVACY_IMPRESSION,
            mastodonAccountId = userAccountId
        )
    }
}
