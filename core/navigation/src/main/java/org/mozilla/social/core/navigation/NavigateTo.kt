package org.mozilla.social.core.navigation

class NavigateTo(private val navigationRelay: MoSoNavigationRelay) {
    operator fun invoke(navDestination: NavDestination) {
        navigationRelay.emitNavEvent(navDestination)
    }
}
