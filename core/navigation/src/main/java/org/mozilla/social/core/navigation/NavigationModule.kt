package org.mozilla.social.core.navigation

import org.koin.dsl.module


val navigationModule = module {
    single<MoSoNavigationRelay> { MoSoNavigationRelay() }
    single { get<MoSoNavigationRelay>().navigationEvents }
    single { NavigateTo(get()) }
}

const val NAVIGATION_EVENTS = "NAVIGATION_EVENTS"