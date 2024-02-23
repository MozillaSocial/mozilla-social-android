package social.firefly.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import social.firefly.common.utils.StringFactory
import timber.log.Timber

class EventRelay {
    private val _navigationEvents = MutableSharedFlow<Event>(
        replay = 0,
        extraBufferCapacity = 10,
    )
    val navigationEvents: SharedFlow<Event>
        get() = _navigationEvents

    fun emitEvent(navDestination: NavigationDestination) {
        emitEvent(Event.NavigateToDestination(navDestination))
    }

    fun emitEvent(navDestination: BottomBarNavigationDestination) {
        emitEvent(Event.NavigateToBottomBarDestination(navDestination))
    }

    fun emitEvent(navDestination: SettingsNavigationDestination) {
        emitEvent(Event.NavigateToSettingsDestination(navDestination))
    }

    fun emitEvent(navDestination: AuthNavigationDestination) {
        emitEvent(Event.NavigateToLoginDestination(navDestination))
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

    data class NavigateToSettingsDestination(val destination: SettingsNavigationDestination) :
        Event()

    data class NavigateToLoginDestination(val destination: AuthNavigationDestination) :
        Event()
}
