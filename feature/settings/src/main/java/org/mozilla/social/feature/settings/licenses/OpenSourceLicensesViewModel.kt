package org.mozilla.social.feature.settings.licenses

import androidx.lifecycle.ViewModel
import org.mozilla.social.feature.settings.SettingsAnalytics

class OpenSourceLicensesViewModel(
    private val analytics: SettingsAnalytics,
) : ViewModel(), OpenSourceLicensesSettingsInteractions {
    override fun onScreenViewed() {
        analytics.openSourceLicencesImpression()
    }
}