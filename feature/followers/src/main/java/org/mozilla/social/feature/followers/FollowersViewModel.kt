package org.mozilla.social.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.ui.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.account.quickview.toQuickViewUiState
import timber.log.Timber

class FollowersViewModel(
    private val accountRepository: AccountRepository,
    private val accountId: String,
    private val followersNavigationCallbacks: FollowersNavigationCallbacks,
) : ViewModel(), FollowersInteractions {

    private val _followersUiState = MutableStateFlow<Resource<List<AccountQuickViewUiState>>>(Resource.Loading())
    val followersUiState: StateFlow<Resource<List<AccountQuickViewUiState>>> = _followersUiState.asStateFlow()

    init {
        loadFollowers()
    }

    private fun loadFollowers() {
        viewModelScope.launch {
            try {
                val uiState = Resource.Loaded(
                    accountRepository
                        .getAccountFollowers(accountId)
                        .map { it.toQuickViewUiState() }
                )
                _followersUiState.edit { uiState }
            } catch (e: Exception) {
                Timber.e(e)
                _followersUiState.edit { Resource.Error(e) }
            }
        }
    }

    override fun onCloseClicked() {
        followersNavigationCallbacks.onCloseClicked()
    }

    override fun onAccountClicked(accountId: String) {
        followersNavigationCallbacks.onAccountClicked(accountId)
    }
}