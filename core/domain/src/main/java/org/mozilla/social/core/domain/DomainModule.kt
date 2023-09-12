package org.mozilla.social.core.domain

import org.koin.dsl.module

val domainModule = module {
    single { HomeTimelineRemoteMediator(get(), get(), get(), get()) }
    single { GetThreadUseCase(get()) }
    single { Login(get(), get(), get(), get()) }
    single { Logout(get(), get()) }
    single { IsSignedInFlow(get()) }
    single { AccountIdFlow(get()) }
}