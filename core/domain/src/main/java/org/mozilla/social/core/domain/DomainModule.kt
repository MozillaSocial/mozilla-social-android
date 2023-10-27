package org.mozilla.social.core.domain

import org.koin.dsl.module
import org.mozilla.social.core.domain.remotemediators.HashTagTimelineRemoteMediator

val domainModule = module {
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
    single { Login(get(), get(), get(), get(), get()) }
    single { Logout(get(), get(), get()) }
    single { IsSignedInFlow(get()) }
    single { AccountIdFlow(get()) }
    single { GetDetailedAccount(get(), get()) }
    single { AccountIdBlocking(get()) }
    single { AccountFlow(get(), get()) }
    single { NavigationRelay() }
}