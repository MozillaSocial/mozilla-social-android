package org.mozilla.social.feature.report.step2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.feature.report.R
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

    ReportScreen2(
        reportInteractions = viewModel
    )

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

@Composable
private fun ReportScreen2(
    reportInteractions: ReportScreen2Interactions,
) {
    MoSoSurface {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            MoSoTopBar(
                title = stringResource(id = R.string.report_screen_title),
                onIconClicked = { reportInteractions.onCloseClicked() }
            )
        }
    }
}