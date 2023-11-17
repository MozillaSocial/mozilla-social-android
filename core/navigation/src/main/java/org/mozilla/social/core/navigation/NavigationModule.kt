package org.mozilla.social.core.navigation

import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.navigation.usecases.ShowSnackbar


val navigationModule = module {
    includes(
        commonModule,
    )

    single { EventRelay() }
    single { NavigationEventFlow(get()) }
    single { NavigateTo(get()) }
    single { PopNavBackstack(get()) }
    single { OpenLink(get()) }
    single { ShowSnackbar(get()) }
}
