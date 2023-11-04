package org.mozilla.social.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.mozilla.social.common.utils.StringFactory
import timber.log.Timber

class EventRelay {
    private val _navigationEvents = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val navigationEvents: SharedFlow<Event>
        get() = _navigationEvents

    fun emitEvent(navDestination: NavigationDestination) {
        emitEvent(Event.NavigateToDestination(navDestination))
    }

    fun emitEvent(navDestination: BottomBarNavigationDestination) {
        emitEvent(Event.NavigateToBottomBarDestination(navDestination))
    }

    fun emitEvent(event: Event) {
        Timber.d("NAVIGATION trying to emit $event")
        _navigationEvents.tryEmit(event)
    }
}

sealed class Event {
    data object PopBackStack : Event()

    data class OpenLink(val url: String) : Event()
    data class ShowSnackbar(val text: StringFactory, val isError: Boolean) : Event()

    data class NavigateToDestination(val destination: NavigationDestination) :
        Event()

    data class NavigateToBottomBarDestination(val destination: BottomBarNavigationDestination) :
        Event()
}
