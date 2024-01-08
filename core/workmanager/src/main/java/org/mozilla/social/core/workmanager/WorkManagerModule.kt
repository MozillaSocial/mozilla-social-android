package org.mozilla.social.core.workmanager

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workManagerModule = module {
    worker {
        DatabasePurgeWorker(
            context = androidApplication(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
}