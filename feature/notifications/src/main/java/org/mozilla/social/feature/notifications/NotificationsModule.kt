package org.mozilla.social.feature.notifications

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val notificationsModule = module {
    viewModelOf(::NotificationsViewModel)
}