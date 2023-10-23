package org.mozilla.social.core.data

import org.koin.dsl.module
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.AppRepository
import org.mozilla.social.core.data.repository.InstanceRepository
import org.mozilla.social.core.data.repository.MediaRepository
import org.mozilla.social.core.data.repository.OauthRepository
import org.mozilla.social.core.data.repository.RecommendationRepository
import org.mozilla.social.core.data.repository.ReportRepository
import org.mozilla.social.core.data.repository.SearchRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.core.network.networkModule

fun repositoryModule(isDebug: Boolean) = module {
    single { AuthCredentialObserver(get(), get()) }
    single { StatusRepository(get(), get(), get()) }
    single { AccountRepository(get(), get()) }
    single { TimelineRepository(get(), get()) }
    single { StatusRepository(get(), get(), get()) }
    single { OauthRepository(get()) }
    single { MediaRepository(get()) }
    single { SearchRepository(get()) }
    single { RecommendationRepository(get()) }
    single { AppRepository(get()) }
    single { InstanceRepository(get()) }
    single { ReportRepository(get()) }
    includes(networkModule(isDebug))
}