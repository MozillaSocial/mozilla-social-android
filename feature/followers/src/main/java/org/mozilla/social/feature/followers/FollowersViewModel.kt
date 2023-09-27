package org.mozilla.social.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.domain.pagingsource.FollowersPagingSource
import org.mozilla.social.core.ui.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.account.quickview.toQuickViewUiState
import timber.log.Timber

class FollowersViewModel(
    private val accountRepository: AccountRepository,
    private val accountId: String,
    private val followersNavigationCallbacks: FollowersNavigationCallbacks,
) : ViewModel(), FollowersInteractions {

    val followers = Pager(
        PagingConfig(
            pageSize = 40,
            initialLoadSize = 40,
        )
    ) {
        FollowersPagingSource(accountRepository, accountId)
    }.flow.map { pagingData ->
        pagingData.map {
            it.toQuickViewUiState()
        }
    }.cachedIn(viewModelScope)

    override fun onCloseClicked() {
        followersNavigationCallbacks.onCloseClicked()
    }

    override fun onAccountClicked(accountId: String) {
        followersNavigationCallbacks.onAccountClicked(accountId)
    }
}