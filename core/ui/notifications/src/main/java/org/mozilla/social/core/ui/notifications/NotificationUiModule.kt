package org.mozilla.social.core.ui.notifications

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val notificationUiModule = module {
    factoryOf(::NotificationCardDelegate)
}