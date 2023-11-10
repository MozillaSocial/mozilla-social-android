package org.mozilla.social.core.navigation.usecases

import org.mozilla.social.core.navigation.BottomBarNavigationDestination
import org.mozilla.social.core.navigation.EventRelay
import org.mozilla.social.core.navigation.AuthNavigationDestination
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.SettingsNavigationDestination

class NavigateTo(
    private val eventRelay: EventRelay
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
