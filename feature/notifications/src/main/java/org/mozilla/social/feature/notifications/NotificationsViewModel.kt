package org.mozilla.social.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.repository.mastodon.NotificationsRepository
import org.mozilla.social.core.repository.paging.NotificationsRemoteMediator

class NotificationsViewModel(
    notificationsRepository: NotificationsRepository,
    notificationsRemoteMediator: NotificationsRemoteMediator,
) : ViewModel(), NotificationsInteractions {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    val feed = notificationsRepository.getNotificationsPager(
        remoteMediator = notificationsRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            //TODO map to ui state
        }
    }.cachedIn(viewModelScope)

    override fun onTabClicked(tab: NotificationsTab) {
        _uiState.edit { copy(
            selectedTab = tab,
        ) }
    }
}