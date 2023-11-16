package org.mozilla.social.feature.settings

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.mozilla.social.feature.settings.about.AboutSettingsViewModel
import org.mozilla.social.feature.settings.account.AccountSettingsViewModel
import org.mozilla.social.feature.settings.contentpreferences.ContentPreferencesSettingsViewModel
import org.mozilla.social.feature.settings.contentpreferences.blockedusers.BlockedUsersViewModel
import org.mozilla.social.feature.settings.contentpreferences.mutedusers.MutedUsersSettingsViewModel
import org.mozilla.social.feature.settings.privacy.PrivacySettingsViewModel

val settingsModule = module {
    viewModel { _ -> SettingsViewModel(get(), get(), get()) }
    viewModel { AccountSettingsViewModel(get(), get(), get()) }
    viewModel { PrivacySettingsViewModel(get()) }
    viewModel { AboutSettingsViewModel(get()) }
    viewModel { ContentPreferencesSettingsViewModel(get()) }
    viewModel { BlockedUsersViewModel(get()) }
    viewModelOf(::BlockedUsersViewModel)
    viewModelOf(::MutedUsersSettingsViewModel)
}