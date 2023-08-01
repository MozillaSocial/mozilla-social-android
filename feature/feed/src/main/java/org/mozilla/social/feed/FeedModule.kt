package org.mozilla.social.feed

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val feedModule = module {
    viewModel { parametersHolder -> FeedViewModel(get(), get(), parametersHolder.get()) }
}