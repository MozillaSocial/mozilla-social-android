package org.mozilla.social.feature.settings

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { parameters -> SettingsViewModel(get(), get(), get(), get(), get(), parameters.get()) }
}