package social.firefly.common

import org.koin.dsl.module
import social.firefly.common.appscope.AppScope

val commonModule =
    module {
        single { AppScope() }
    }
