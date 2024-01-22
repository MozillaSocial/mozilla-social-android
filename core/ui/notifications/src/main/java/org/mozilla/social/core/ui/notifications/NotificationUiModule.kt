package org.mozilla.social.core.ui.notifications

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.mozilla.social.core.navigation.navigationModule

val notificationUiModule = module {
    includes(
        navigationModule,
    )

    factoryOf(::NotificationCardDelegate)
}