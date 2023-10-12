package org.mozilla.social.feature.report

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mozilla.social.feature.report.step1.ReportScreen1ViewModel
import org.mozilla.social.feature.report.step2.ReportScreen2ViewModel
import org.mozilla.social.feature.report.step3.ReportScreen3ViewModel

val reportModule = module {
    viewModel { parametersHolder ->
        ReportScreen1ViewModel(
            get(),
            get(),
            get(),
            parametersHolder[0],
            parametersHolder[1],
            parametersHolder[2],
            parametersHolder[3],
        )
    }
    viewModel { parametersHolder ->
        ReportScreen2ViewModel(
        )
    }
    viewModel { parametersHolder ->
        ReportScreen3ViewModel(
        )
    }
}