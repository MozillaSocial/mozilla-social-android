package social.firefly.core.workmanager

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule

val workManagerModule = module {
    includes(
        mastodonUsecaseModule,
        mastodonRepositoryModule,
    )

    worker {
        DatabasePurgeWorker(
            context = androidApplication(),
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
}