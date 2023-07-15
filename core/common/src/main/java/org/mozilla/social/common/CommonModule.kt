package org.mozilla.social.common

import org.koin.dsl.module
import org.mozilla.social.common.logging.Log
import org.mozilla.social.common.logging.TimberLog

val commonModule = module {
    single<Log> { TimberLog(true) }
}