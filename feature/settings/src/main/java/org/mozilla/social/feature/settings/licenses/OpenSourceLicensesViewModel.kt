package org.mozilla.social.feature.settings.licenses

import androidx.lifecycle.ViewModel
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.feature.settings.SettingsInteractions

class OpenSourceLicensesViewModel(
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel() {

    private val userAccountId: String = getLoggedInUserAccountId()
}