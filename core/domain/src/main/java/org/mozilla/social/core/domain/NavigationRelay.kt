package org.mozilla.social.core.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationRelay {
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    suspend fun navigateToLogin() {
        _navigationEvents.emit(NavigationEvent.NavigateToLogin)
    }
}

sealed class NavigationEvent {
    data object NavigateToLogin : NavigationEvent()
}