package org.mozilla.social.core.domain

import org.koin.dsl.module
import org.mozilla.social.core.domain.remotemediators.HashTagTimelineRemoteMediator
import org.mozilla.social.core.domain.remotemediators.HomeTimelineRemoteMediator

val domainModule = module {
    single { HomeTimelineRemoteMediator(get(), get(), get(), get()) }
    factory { parametersHolder ->
        HashTagTimelineRemoteMediator(
            get(),
            get(),
            get(),
            get(),
            parametersHolder[0]
        )
    }
    single { GetThreadUseCase(get()) }
    single { Login(get(), get(), get(), get()) }
    single { Logout(get(), get()) }
    single { IsSignedInFlow(get()) }
    single { AccountIdFlow(get()) }
}