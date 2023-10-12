package org.mozilla.social.feature.report.step1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoCheckBox
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoRadioButton
import org.mozilla.social.core.designsystem.component.MoSoToast
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.transparentTextFieldColors
import org.mozilla.social.feature.report.R
import org.mozilla.social.feature.report.ReportInteractions
import org.mozilla.social.feature.report.ReportTarget
import org.mozilla.social.feature.report.ReportType
import org.mozilla.social.model.InstanceRule

@Composable
internal fun ReportScreen1(
    onReported: () -> Unit,
    onCloseClicked: () -> Unit,
    onNextClicked: (reportType: ReportType) -> Unit,
    reportAccountId: String,
    reportStatusId: String?,
    viewModel: ReportScreen1ViewModel = koinViewModel(parameters = {
        parametersOf(
            onReported,
            onCloseClicked,
            reportAccountId,
            reportStatusId,
        )
    })
) {
    ReportScreen1(
        reportTarget = if (reportStatusId != null) {
            ReportTarget.POST
        } else {
            ReportTarget.ACCOUNT
        },
        instanceRules = viewModel.instanceRules.collectAsState().value,
        selectedReportType = viewModel.selectedReportType.collectAsState().value,
        checkedRules = viewModel.checkedRules.collectAsState().value,
        additionalCommentText = viewModel.additionalCommentText.collectAsState().value,
        reportInteractions = viewModel
    )

    MoSoToast(toastMessage = viewModel.errorToastMessage)
}

@Composable
private fun ReportScreen1(
    reportTarget: ReportTarget,
    instanceRules: List<InstanceRule>,
    selectedReportType: ReportType?,
    checkedRules: List<InstanceRule>,
    additionalCommentText: String,
    reportInteractions: ReportInteractions,
) {
    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars))
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            MoSoTopBar(
                title = stringResource(id = R.string.report_screen_title),
                onIconClicked = { reportInteractions.onCloseClicked() }
            )
            MoSoDivider()
            MainContent(
                reportTarget = reportTarget,
                instanceRules = instanceRules,
                selectedReportType = selectedReportType,
                checkedRules = checkedRules,
                additionalCommentText = additionalCommentText,
                reportInteractions = reportInteractions,
            )
        }
    }
}

@Composable
private fun MainContent(
    reportTarget: ReportTarget,
    instanceRules: List<InstanceRule>,
    selectedReportType: ReportType?,
    checkedRules: List<InstanceRule>,
    additionalCommentText: String,
    reportInteractions: ReportInteractions,
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = stringResource(id = if (reportTarget == ReportTarget.POST) {
                R.string.report_instructions_for_post
            } else {
                R.string.report_instructions_for_account
            }),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = stringResource(id = R.string.choose_best_match),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.padding(4.dp))
        SelectableReportType(
            reportType = ReportType.SPAM,
            title = stringResource(id = R.string.report_reason_spam),
            selectedReportType = selectedReportType,
            reportInteractions = reportInteractions,
        ) {
            Text(text = stringResource(id = R.string.report_reason_spam_description))
        }

        SelectableReportType(
            reportType = ReportType.VIOLATION,
            title = stringResource(id = R.string.report_reason_violation),
            selectedReportType = selectedReportType,
            reportInteractions = reportInteractions,
        ) {
            if (selectedReportType == ReportType.VIOLATION) {
                instanceRules.forEach { instanceRule ->
                    CheckableInstanceRule(
                        checked = checkedRules.contains(instanceRule),
                        instanceRule = instanceRule,
                        reportInteractions = reportInteractions,
                    )
                }
            }
        }

        SelectableReportType(
            reportType = ReportType.OTHER,
            title = stringResource(id = R.string.report_reason_other),
            selectedReportType = selectedReportType,
            reportInteractions = reportInteractions,
        ) {
            Text(text = stringResource(id = R.string.report_reason_other_description))
        }

        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = stringResource(id = R.string.extra_info_prompt))
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(4.dp)
                ),
            value = additionalCommentText,
            colors = transparentTextFieldColors(),
            label = {
                Text(text = stringResource(id = R.string.extra_info_text_field_label))
            },
            onValueChange = { reportInteractions.onAdditionCommentTextChanged(it) },
        )
        Spacer(modifier = Modifier.padding(8.dp))

        MoSoButton(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = selectedReportType != null,
            onClick = { reportInteractions.onReportClicked() }
        ) {
            Text(text = stringResource(id = R.string.report_button))
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun SelectableReportType(
    reportType: ReportType,
    title: String,
    selectedReportType: ReportType?,
    reportInteractions: ReportInteractions,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .clickable { reportInteractions.onReportTypeSelected(reportType) }
    ) {
        MoSoRadioButton(
            modifier = Modifier
                .size(20.dp),
            selected = selectedReportType == reportType,
            onClick = { reportInteractions.onReportTypeSelected(reportType) }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )
            content()
        }

    }
}

@Composable
private fun CheckableInstanceRule(
    checked: Boolean,
    instanceRule: InstanceRule,
    reportInteractions: ReportInteractions,
) {
    Row(
        Modifier
            .clickable {
                reportInteractions.onServerRuleClicked(instanceRule)
            }
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        MoSoCheckBox(
            modifier = Modifier
                .size(20.dp),
            checked = checked,
            onCheckedChange = { reportInteractions.onServerRuleClicked(instanceRule) }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = instanceRule.text,
        )
    }
}

@Preview
@Composable
private fun ReportScreenPreview() {
    val noDummies = InstanceRule(
        1,
        "no dummies"
    )
    val noDogs = InstanceRule(
        2,
        "no dogs"
    )
    MoSoTheme {
        ReportScreen1(
            reportTarget = ReportTarget.POST,
            instanceRules = listOf(
                noDummies,
                noDogs
            ),
            selectedReportType = ReportType.VIOLATION,
            checkedRules = listOf(
                noDogs,
            ),
            additionalCommentText = "",
            reportInteractions = object : ReportInteractions {}
        )
    }
}

@Preview
@Composable
private fun ReportScreenPreviewDarkMode() {
    val noDummies = InstanceRule(
        1,
        "no dummies"
    )
    val noDogs = InstanceRule(
        2,
        "no dogs"
    )
    MoSoTheme(
        darkTheme = true
    ) {
        ReportScreen1(
            reportTarget = ReportTarget.POST,
            instanceRules = listOf(
                noDummies,
                noDogs
            ),
            selectedReportType = ReportType.VIOLATION,
            checkedRules = listOf(
                noDogs,
            ),
            additionalCommentText = "",
            reportInteractions = object : ReportInteractions {}
        )
    }
}