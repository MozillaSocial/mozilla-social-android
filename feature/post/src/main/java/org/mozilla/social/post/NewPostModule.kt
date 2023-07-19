package org.mozilla.social.post

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPostModule = module {
    viewModel { parametersHolder ->  NewPostViewModel(get(), parametersHolder.get()) }
}