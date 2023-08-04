package org.mozilla.social.core.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.core.ui.poll.PollViewModel

val uiModule = module {
    viewModel { parameters -> PollViewModel(get(), parameters.get()) }
}