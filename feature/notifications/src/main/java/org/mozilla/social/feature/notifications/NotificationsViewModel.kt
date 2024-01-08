package org.mozilla.social.feature.notifications

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.social.common.utils.edit

class NotificationsViewModel : ViewModel(

), NotificationsInteractions {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState = _uiState.asStateFlow()

    override fun onTabClicked(tab: NotificationsTab) {
        _uiState.edit { copy(
            selectedTab = tab,
        ) }
    }
}