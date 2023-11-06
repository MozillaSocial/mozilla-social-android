package org.mozilla.social.feature.settings

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.feature.settings.account.AccountSettingsViewModel
import org.mozilla.social.feature.settings.privacy.PrivacySettingsViewModel

val settingsModule = module {
    viewModel { SettingsViewModel(get()) }
    viewModel { AccountSettingsViewModel(get(), get(), get()) }
    viewModel { PrivacySettingsViewModel(get()) }
}