package org.mozilla.social.feature.settings.licenses

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers

class OpenSourceLicensesViewModel(
    private val analytics: Analytics,
) : ViewModel(), OpenSourceLicensesSettingsInteractions {
    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_CONTENT_OPEN_SOURCE_LICENSE,
        )
    }
}