package org.mozilla.social.core.navigation

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.NavigateToAccount
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.navigation.usecases.ShowSnackbar

val navigationModule =
    module {
        includes(
            commonModule,
        )

        singleOf(::EventRelay)
        singleOf(::NavigationEventFlow)
        singleOf(::NavigateTo)
        singleOf(::PopNavBackstack)
        singleOf(::OpenLink)
        singleOf(::ShowSnackbar)
        singleOf(::NavigateToAccount)
    }
