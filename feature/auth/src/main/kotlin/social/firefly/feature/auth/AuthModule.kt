package social.firefly.feature.auth

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule
import social.firefly.feature.auth.chooseServer.ChooseServerViewModel
import social.firefly.feature.auth.login.LoginViewModel

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

        viewModelOf(::LoginViewModel)
        viewModelOf(::ChooseServerViewModel)
    }
