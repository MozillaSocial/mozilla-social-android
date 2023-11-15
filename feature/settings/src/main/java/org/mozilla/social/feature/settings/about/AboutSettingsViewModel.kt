package org.mozilla.social.feature.settings.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.mozilla.social.core.model.Instance
import org.mozilla.social.core.repository.mastodon.InstanceRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions


class AboutSettingsViewModel(private val instanceRepository: InstanceRepository) : ViewModel(),
    HtmlContentInteractions {
    val aboutSettings: StateFlow<AboutSettings?> = flow {
        val instanceDeferred = viewModelScope.async { instanceRepository.getInstance() }
        val extendedDescriptionDeferred =
            viewModelScope.async { instanceRepository.getExtendedDescription() }

        val instance = instanceDeferred.await()
        val extendedDescription = extendedDescriptionDeferred.await()

        emit(instance.toAboutSettings(extendedDescription))

    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

fun Instance.toAboutSettings(extendedDescription: String) = AboutSettings(
    title = title,
    administeredBy = contactAccount?.toQuickViewUiState(),
    contactEmail = contactEmail,
    extendedDescription = extendedDescription,
    thumbnailUrl = thumbnail,
)