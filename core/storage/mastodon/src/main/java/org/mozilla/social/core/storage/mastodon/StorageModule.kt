package org.mozilla.social.core.storage.mastodon

import org.koin.dsl.module
import org.mozilla.social.core.database.databaseModule
import org.mozilla.social.core.storage.mastodon.timeline.LocalAccountTimelineRepository
import org.mozilla.social.core.storage.mastodon.timeline.LocalFederatedTimelineRepository
import org.mozilla.social.core.storage.mastodon.timeline.LocalHashtagTimelineRepository
import org.mozilla.social.core.storage.mastodon.timeline.LocalHomeTimelineRepository
import org.mozilla.social.core.storage.mastodon.timeline.LocalLocalTimelineRepository
import org.mozilla.social.core.storage.mastodon.timeline.LocalTimelineRepository

val mastodonStorageModule = module {
    single { LocalAccountRepository(get()) }
    single { DatabaseDelegate(get()) }
    single { LocalAccountTimelineRepository(get()) }
    single { LocalFederatedTimelineRepository(get()) }
    single { LocalHashtagTimelineRepository(get()) }
    single { LocalHomeTimelineRepository(get()) }
    single { LocalLocalTimelineRepository(get()) }
    single {
        LocalTimelineRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single { LocalPollRepository(get()) }
    single { LocalAccountRepository(get()) }
    single { LocalRelationshipRepository(get()) }
    single { LocalStatusRepository(get()) }
    includes(databaseModule)
}
