package org.mozilla.social

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.core.analytics.AppAnalytics
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule

val mainModule = module {
    includes(
        mastodonUsecaseModule,
        navigationModule,
        dataStoreModule,
        analyticsModule,
    )
    
    viewModel { MainViewModel(get(), get(), get(), get()) }
}
