package social.firefly.core.navigation

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.NavigateToAccount
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.navigation.usecases.PopNavBackstack
import social.firefly.core.navigation.usecases.ShowSnackbar

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
