package org.mozilla.social.feature.discover

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val discoverModule = module {
    viewModel {
        DiscoverViewModel()
    }
}