package org.mozilla.social.core.navigation

class NavigateTo(
    private val navigationRelay: NavigationRelay<NavDestination>
) {
    operator fun invoke(navDestination: NavDestination) {
        navigationRelay.emitNavEvent(navDestination)
    }
}
