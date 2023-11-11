package org.mozilla.social.core.usecase.mozilla

import org.koin.dsl.module

val mozillaUsecaseModule = module {

    single { GetRecommendations(get()) }

}