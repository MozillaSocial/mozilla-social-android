package org.mozilla.social.feature.hashtag

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val hashTagModule = module {
    viewModel { parametersHolder ->
        HashTagViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            parametersHolder[0],
            parametersHolder[1],
        )
    }
}