package org.mozilla.social.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.data.repository.InstanceRepository
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import org.mozilla.social.core.datastore.MastodonInstance
import org.mozilla.social.core.domain.AccountFlow
import org.mozilla.social.core.domain.Logout
import org.mozilla.social.core.navigation.SettingsNavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.model.Instance

class SettingsViewModel(
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val logout: Logout,
    private val navigateTo: NavigateTo,
    getAccountFlow: AccountFlow,
) : ViewModel() {

    private val _isAnalyticsToggledOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isAnalyticsToggledOn = _isAnalyticsToggledOn.asStateFlow()

    val userHeader = getAccountFlow().mapLatest {
        UserHeader(
            avatarUrl = it.avatarUrl,
            accountName = it.acct,
            url = it.url,
        )
    }

    init {
        viewModelScope.launch {
            appPreferencesDatastore.trackAnalytics.collectLatest { enabled ->
                _isAnalyticsToggledOn.value = enabled
            }
        }
    }

    fun toggleAnalytics() {
        viewModelScope.launch {
            saveSettingsChanges(_isAnalyticsToggledOn.value.not())
        }
    }

    private suspend fun saveSettingsChanges(optToggle: Boolean) {
        appPreferencesDatastore.toggleTrackAnalytics(optToggle)
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logout()
        }
    }

    fun onAboutClicked() {
        navigateTo(SettingsNavigationDestination.AboutSettings)
    }

    fun onAccountClicked() {
        navigateTo(SettingsNavigationDestination.AccountSettings)
    }

    fun onPrivacyClicked() {
        navigateTo(SettingsNavigationDestination.PrivacySettings)
    }

    @Suppress("EmptyFunctionBlock")
    fun onProfileSettingsClicked() {

    }
}

data class UserHeader(val avatarUrl: String, val accountName: String, val url: String)