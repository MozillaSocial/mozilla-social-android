package org.mozilla.social.feature.settings.licenses

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId

class OpenSourceLicensesViewModel(
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), OpenSourceLicensesSettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_CONTENT_OPEN_SOURCE_LICENSE,
        )
    }
}