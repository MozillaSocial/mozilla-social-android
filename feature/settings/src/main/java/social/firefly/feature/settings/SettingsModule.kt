package social.firefly.feature.settings

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.repository.paging.pagingModule
import social.firefly.core.usecase.mastodon.htmlcontent.DefaultHtmlInteractions
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule
import social.firefly.core.workmanager.workManagerModule
import social.firefly.feature.settings.about.AboutSettingsViewModel
import social.firefly.feature.settings.account.AccountSettingsViewModel
import social.firefly.feature.settings.contentpreferences.ContentPreferencesSettingsViewModel
import social.firefly.feature.settings.contentpreferences.blockedusers.BlockedUsersViewModel
import social.firefly.feature.settings.contentpreferences.mutedusers.MutedUsersSettingsViewModel
import social.firefly.feature.settings.licenses.OpenSourceLicensesViewModel
import social.firefly.feature.settings.privacy.PrivacySettingsViewModel

val settingsModule =
    module {
        includes(
            commonModule,
            analyticsModule,
            dataStoreModule,
            navigationModule,
            mastodonRepositoryModule,
            mastodonUsecaseModule,
            workManagerModule,
            pagingModule,
        )

        viewModelOf(::SettingsViewModel)
        viewModelOf(::AccountSettingsViewModel)
        viewModelOf(::PrivacySettingsViewModel)
        viewModelOf(::AboutSettingsViewModel)
        viewModelOf(::ContentPreferencesSettingsViewModel)
        viewModelOf(::BlockedUsersViewModel)
        viewModelOf(::MutedUsersSettingsViewModel)
        viewModelOf(::OpenSourceLicensesViewModel)
        singleOf(::DefaultHtmlInteractions)
    }
