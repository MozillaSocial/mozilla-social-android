package org.mozilla.social.search

import org.koin.dsl.module
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule

val searchModule =
    module {
        includes(
            dataStoreModule,
            mastodonRepositoryModule,
            navigationModule,
        )
    }
