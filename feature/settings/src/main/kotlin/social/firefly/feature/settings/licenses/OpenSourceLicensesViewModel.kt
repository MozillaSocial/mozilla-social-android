package social.firefly.feature.settings.licenses

import androidx.lifecycle.ViewModel
import social.firefly.core.analytics.SettingsAnalytics

class OpenSourceLicensesViewModel(
    private val analytics: SettingsAnalytics,
) : ViewModel(), OpenSourceLicensesSettingsInteractions {
    override fun onScreenViewed() {
        analytics.openSourceLicencesViewed()
    }
}