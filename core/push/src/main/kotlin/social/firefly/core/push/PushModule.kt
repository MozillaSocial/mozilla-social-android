package social.firefly.core.push

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule

val pushModule = module {
    includes(
        commonModule,
        mastodonRepositoryModule,
        dataStoreModule,
    )

    singleOf(::KeyManager)
}