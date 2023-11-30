package org.mozilla.social.feature.settings.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.Instance
import org.mozilla.social.core.repository.mastodon.InstanceRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.core.usecase.mastodon.account.GetDetailedAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.feature.settings.SettingsInteractions

class AboutSettingsViewModel(
    private val instanceRepository: InstanceRepository,
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), HtmlContentInteractions, SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()
    val aboutSettings: StateFlow<AboutSettings?> =
        flow {
            val instanceDeferred = viewModelScope.async { instanceRepository.getInstance() }
            val extendedDescriptionDeferred =
                viewModelScope.async { instanceRepository.getExtendedDescription() }

            val instance = instanceDeferred.await()
            val extendedDescription = extendedDescriptionDeferred.await()

            emit(instance.toAboutSettings(extendedDescription))
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_ABOUT_IMPRESSION,
            mastodonAccountId = userAccountId
        )
    }
}

fun Instance.toAboutSettings(extendedDescription: String) =
    AboutSettings(
        title = title,
        administeredBy = contactAccount?.toQuickViewUiState(),
        contactEmail = contactEmail,
        extendedDescription = extendedDescription,
        thumbnailUrl = thumbnail,
    )
