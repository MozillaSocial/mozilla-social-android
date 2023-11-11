package org.mozilla.social.feature.discover

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.usecase.mozilla.mozillaUsecaseModule

val discoverModule = module {
    viewModel {
        DiscoverViewModel(
            get(),
            get(),
        )
    }

    includes(mozillaUsecaseModule)
}