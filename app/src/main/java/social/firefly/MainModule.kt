package social.firefly

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val mainModule = module {
    includes(
        mastodonUsecaseModule,
        navigationModule,
        dataStoreModule,
        analyticsModule,
    )

    viewModelOf(::MainViewModel)
}
