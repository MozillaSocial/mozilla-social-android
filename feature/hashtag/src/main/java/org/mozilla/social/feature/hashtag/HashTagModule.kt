package org.mozilla.social.feature.hashtag

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val hashTagModule = module {
    viewModel { parametersHolder ->
        HashTagViewModel(
            hashTag = parametersHolder[0],
            socialDatabase = get(),
            accountIdFlow = get(),
        )
    }
}