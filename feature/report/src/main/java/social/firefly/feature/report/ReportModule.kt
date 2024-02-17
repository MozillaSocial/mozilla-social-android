package social.firefly.feature.report

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import social.firefly.common.commonModule
import social.firefly.core.analytics.analyticsModule
import social.firefly.core.datastore.dataStoreModule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.repository.mastodon.mastodonRepositoryModule
import social.firefly.core.usecase.mastodon.mastodonUsecaseModule
import social.firefly.feature.report.step1.ReportScreen1ViewModel
import social.firefly.feature.report.step2.ReportScreen2ViewModel
import social.firefly.feature.report.step3.ReportScreen3ViewModel

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
