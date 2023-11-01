package org.mozilla.social.feature.hashtag

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val hashTagModule = module {
    viewModel { parametersHolder ->
        HashTagViewModel(
            hashTag = parametersHolder[0],
            statusRepository = get(),
            accountRepository = get(),
            log = get(),
            socialDatabase = get(),
            accountIdFlow = get(),
            navigateTo = get(),
            openLink = get(),
        )
    }
}