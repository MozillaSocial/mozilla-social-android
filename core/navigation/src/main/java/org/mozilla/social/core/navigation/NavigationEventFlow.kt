package org.mozilla.social.core.navigation

import kotlinx.coroutines.flow.SharedFlow


class NavigationEventFlow(private val navigationRelay: NavigationRelay<NavDestination>) {
    operator fun invoke(): SharedFlow<NavDestination> = navigationRelay.navigationEvents
}