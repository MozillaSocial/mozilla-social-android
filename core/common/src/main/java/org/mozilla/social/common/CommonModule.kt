package org.mozilla.social.common

import org.koin.dsl.module
import org.mozilla.social.common.appscope.AppScope

val commonModule = module {
    single { AppScope() }
}