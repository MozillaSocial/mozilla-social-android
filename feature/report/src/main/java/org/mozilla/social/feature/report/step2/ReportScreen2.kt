package org.mozilla.social.feature.report.step2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.theme.MoSoTheme
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
            reportStatusId,
            reportType,
            checkedInstanceRules,
            additionalText,
            sendToExternalServer,
        )
    })
) {

    ReportScreen2(
        reportAccountHandle = reportAccountHandle,
        reportInteractions = viewModel
    )

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

@Composable
private fun ReportScreen2(
    reportAccountHandle: String,
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

            TopContent(reportAccountHandle = reportAccountHandle)

            MoSoDivider()

            MiddleContent(
                modifier = Modifier.weight(1f)
            )

            MoSoDivider()

            BottomContent(
                reportInteractions = reportInteractions,
            )
        }
    }
}

@Composable
private fun TopContent(
    reportAccountHandle: String,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.report_prompt, "@$reportAccountHandle"),
            style = MoSoTheme.typography.bodyMedium,
        )

        Text(
            text = stringResource(id = R.string.screen_2_prompt),
            style = MoSoTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun MiddleContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {

    }
}

@Composable
private fun BottomContent(
    reportInteractions: ReportScreen2Interactions,
) {
    MoSoButton(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        onClick = { reportInteractions.onReportClicked() }
    ) {
        Text(text = stringResource(id = R.string.submit_report_button))
    }
}