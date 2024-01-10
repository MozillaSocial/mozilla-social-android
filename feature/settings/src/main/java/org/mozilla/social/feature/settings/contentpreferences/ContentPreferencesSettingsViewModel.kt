package org.mozilla.social.feature.settings.contentpreferences

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.feature.settings.SettingsInteractions

class ContentPreferencesSettingsViewModel(
    private val navigateTo: NavigateTo,
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), ContentPreferencesSettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    fun onMutedUsersClicked() {
        navigateTo(SettingsNavigationDestination.MutedUsersSettings)
    }

    fun onBlockedUsersClicked() {
        navigateTo(SettingsNavigationDestination.BlockedUsersSettings)
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_CONTENT_PREFERENCES_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}
