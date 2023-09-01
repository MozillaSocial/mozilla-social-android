package org.mozilla.social.feature.report

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val reportModule = module {
    viewModel { parametersHolder ->
        ReportViewModel(
            parametersHolder[0],
            parametersHolder[1],
            parametersHolder[2],
            parametersHolder[3],
        )
    }
}