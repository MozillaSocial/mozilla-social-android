package org.mozilla.social.common.appscope

import org.koin.dsl.module

val appScopeModule = module {
    single { AppScope() }
}