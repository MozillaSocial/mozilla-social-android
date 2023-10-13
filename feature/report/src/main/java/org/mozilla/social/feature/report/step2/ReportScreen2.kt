@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step2

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.feature.report.ReportType
import org.mozilla.social.model.InstanceRule

@Composable
internal fun ReportScreen2(
    onCloseClicked: () -> Unit,
    onReportSubmitted: () -> Unit,
    reportAccountId: String,
    reportAccountHandle: String,
    reportStatusId: String?,
    reportType: ReportType,
    checkedInstanceRules: List<InstanceRule>,
    additionalText: String,
    sendToExternalServer: Boolean,
    viewModel: ReportScreen2ViewModel = koinViewModel(parameters = {
        parametersOf(
            onCloseClicked,
            onReportSubmitted,
            reportAccountId,
            reportAccountHandle,
            reportStatusId,
            reportType,
            checkedInstanceRules,
            additionalText,
            sendToExternalServer,
        )
    })
) {

    ReportScreen2()

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

@Composable
private fun ReportScreen2() {
    MoSoButton(onClick = { /*TODO*/ }) {

    }
}