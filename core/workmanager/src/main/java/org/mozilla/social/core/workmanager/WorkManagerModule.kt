package org.mozilla.social.core.workmanager

import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val workManagerModule = module {
    workerOf(::DatabasePurgeWorker)
}