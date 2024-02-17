package social.firefly.feature.settings.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import social.firefly.common.Resource
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.model.Instance
import social.firefly.core.navigation.SettingsNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.InstanceRepository
import social.firefly.core.ui.common.account.quickview.toQuickViewUiState
import social.firefly.core.ui.htmlcontent.HtmlContentInteractions
import social.firefly.core.usecase.mastodon.htmlcontent.DefaultHtmlInteractions
import timber.log.Timber

class AboutSettingsViewModel(
    private val analytics: SettingsAnalytics,
    private val navigateTo: NavigateTo,
    private val instanceRepository: InstanceRepository,
    private val defaultHtmlInteractions: DefaultHtmlInteractions,
) : ViewModel(),
    AboutInteractions,
    HtmlContentInteractions by defaultHtmlInteractions {

    private val _aboutSettings = MutableStateFlow<Resource<AboutSettings>>(Resource.Loading())
    val aboutSettings = _aboutSettings.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadServerInfo()
    }

    private fun loadServerInfo() {
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            _aboutSettings.update { Resource.Loading() }
            try {
                val instanceDeferred = viewModelScope.async { instanceRepository.getInstance() }
                val extendedDescriptionDeferred =
                    viewModelScope.async { instanceRepository.getExtendedDescription() }

                val instance = instanceDeferred.await()
                val extendedDescription = extendedDescriptionDeferred.await()
                _aboutSettings.update { Resource.Loaded(instance.toAboutSettings(extendedDescription)) }
            } catch (e: Exception) {
                _aboutSettings.update { Resource.Error(e) }
                Timber.e(e)
            }
        }
    }

    override fun onScreenViewed() {
        analytics.aboutSettingsViewed()
    }

    override fun onOpenSourceLicensesClicked() {
        navigateTo(SettingsNavigationDestination.OpenSourceLicensesSettings)
    }

    override fun onRetryClicked() {
        loadServerInfo()
    }
}

fun Instance.toAboutSettings(extendedDescription: String) =
    AboutSettings(
        title = title,
        administeredBy = contactAccount?.toQuickViewUiState(),
        contactEmail = contactEmail,
        extendedDescription = extendedDescription,
        thumbnailUrl = thumbnail,
        rules = rules,
    )
