package org.mozilla.social.core.domain

import org.koin.dsl.module

val domainModule = module {
    single { HomeTimelinePagingSource(get(), get()) }
}