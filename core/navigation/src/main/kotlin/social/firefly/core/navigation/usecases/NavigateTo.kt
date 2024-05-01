package social.firefly.core.navigation.usecases

import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.BottomBarNavigationDestination
import social.firefly.core.navigation.EventRelay
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.SettingsNavigationDestination

class NavigateTo(
    private val eventRelay: EventRelay,
) {
    operator fun invoke(navDestination: NavigationDestination) {
        eventRelay.emitEvent(navDestination)
    }

    operator fun invoke(navDestination: BottomBarNavigationDestination) {
        eventRelay.emitEvent(navDestination)
    }

    operator fun invoke(navDestination: SettingsNavigationDestination) {
        eventRelay.emitEvent(navDestination)
    }

    operator fun invoke(navDestination: AuthNavigationDestination) {
        eventRelay.emitEvent(navDestination)
    }
}
