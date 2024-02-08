package org.mozilla.social.feature.auth

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule
import org.mozilla.social.feature.auth.chooseServer.ChooseServerViewModel
import org.mozilla.social.feature.auth.login.LoginViewModel
import org.mozilla.social.core.analytics.LoginAnalytics

val authModule =
    module {
        includes(
            commonModule,
            dataStoreModule,
            mastodonUsecaseModule,
            mastodonRepositoryModule,
            navigationModule,
            analyticsModule,
        )

        viewModel { LoginViewModel(get(), get(), get(), get()) }
        viewModelOf(::ChooseServerViewModel)
    }
