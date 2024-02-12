package org.mozilla.social.feature.report

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.mozilla.social.common.commonModule
import org.mozilla.social.core.analytics.ReportScreenAnalytics
import org.mozilla.social.core.analytics.analyticsModule
import org.mozilla.social.core.datastore.dataStoreModule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.repository.mastodon.mastodonRepositoryModule
import org.mozilla.social.core.usecase.mastodon.mastodonUsecaseModule
import org.mozilla.social.feature.report.step1.ReportScreen1ViewModel
import org.mozilla.social.feature.report.step2.ReportScreen2ViewModel
import org.mozilla.social.feature.report.step3.ReportScreen3ViewModel

val reportModule =
    module {
        includes(
            commonModule,
            mastodonRepositoryModule,
            dataStoreModule,
            navigationModule,
            analyticsModule,
            mastodonUsecaseModule,
        )

        viewModel { parametersHolder ->
            ReportScreen1ViewModel(
                get(),
                get(),
                parametersHolder[0],
                parametersHolder[1],
                parametersHolder[2],
                parametersHolder[3],
                parametersHolder[4],
            )
        }
        viewModel { parametersHolder ->
            ReportScreen2ViewModel(
                report = get(),
                accountRepository = get(),
                onClose = parametersHolder[0],
                onReportSubmitted = parametersHolder[1],
                reportAccountId = parametersHolder[2],
                reportAccountHandle = parametersHolder[3],
                reportStatusId = parametersHolder[4],
                reportType = parametersHolder[5],
                checkedInstanceRules = parametersHolder[6],
                additionalText = parametersHolder[7],
                sendToExternalServer = parametersHolder[8],
            )
        }
        viewModel { parametersHolder ->
            ReportScreen3ViewModel(
                unfollowAccount = get(),
                blockAccount = get(),
                muteAccount = get(),
                getLoggedInUserAccountId = get(),
                doneClicked = parametersHolder[0],
                closeClicked = parametersHolder[1],
                reportAccountId = parametersHolder[2],
            )
        }
    }
