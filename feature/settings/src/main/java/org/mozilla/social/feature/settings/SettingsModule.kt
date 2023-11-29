package org.mozilla.social.feature.settings

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule
import org.mozilla.social.feature.settings.about.AboutSettingsScreen
import org.mozilla.social.feature.settings.about.AboutSettingsViewModel
import org.mozilla.social.feature.settings.account.AccountSettingsViewModel
import org.mozilla.social.feature.settings.contentpreferences.ContentPreferencesSettingsViewModel
import org.mozilla.social.feature.settings.contentpreferences.blockedusers.BlockedUsersViewModel
import org.mozilla.social.feature.settings.contentpreferences.mutedusers.MutedUsersSettingsViewModel
import org.mozilla.social.feature.settings.privacy.PrivacySettingsViewModel

val settingsModule =
    module {
        includes(
            commonModule,
            analyticsModule,
            dataStoreModule,
            navigationModule,
            mastodonRepositoryModule,
            mastodonUsecaseModule,
        )

        viewModel { _ -> SettingsViewModel(get(), get(), get()) }
        viewModel { AccountSettingsViewModel(get(), get(), get()) }
        viewModelOf(::PrivacySettingsViewModel)
        viewModelOf(::AboutSettingsViewModel)
        viewModelOf(::ContentPreferencesSettingsViewModel)
        viewModelOf(::BlockedUsersViewModel)
        viewModelOf(::MutedUsersSettingsViewModel)
    }
