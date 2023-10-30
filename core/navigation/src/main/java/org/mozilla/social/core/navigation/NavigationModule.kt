package org.mozilla.social.core.navigation

import org.koin.dsl.module


val navigationModule = module {
    single<NavigationRelay<NavDestination>> { NavigationRelay() }
    single { NavigationEventFlow(get()) }
    single { NavigateTo(get()) }
}

const val NAVIGATION_EVENTS = "NAVIGATION_EVENTS"