package org.mozilla.social.feature.report

import android.widget.Toast
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.component.MoSoTopBar
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.transparentTextFieldColors
import org.mozilla.social.model.InstanceRule

@Composable
fun ReportRoute(
    onReported: () -> Unit,
    onCloseClicked: () -> Unit,
    reportAccountId: String,
    reportStatusId: String?,
    viewModel: ReportViewModel = koinViewModel(parameters = { parametersOf(
        onReported,
        onCloseClicked,
        reportAccountId,
        reportStatusId,
    ) })
) {
    ReportScreen(
        instanceRules = viewModel.instanceRules.collectAsState().value,
        selectedReportType = viewModel.selectedReportType.collectAsState().value,
        checkedRules = viewModel.checkedRules.collectAsState().value,
        additionalCommentText = viewModel.additionalCommentText.collectAsState().value,
        reportInteractions = viewModel
    )

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.errorToastMessage.collect {
            Toast.makeText(context, it.build(context), Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun ReportScreen(
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
                onCloseClicked = { reportInteractions.onCloseClicked() }
            )
            Divider()
            MainContent(
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
            text = stringResource(id = R.string.report_instructions),
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
            instanceRules.forEach { instanceRule ->
                CheckableInstanceRule(
                    enabled = selectedReportType == ReportType.VIOLATION,
                    checked = checkedRules.contains(instanceRule),
                    instanceRule = instanceRule,
                    reportInteractions = reportInteractions,
                )
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

        Button(
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
        RadioButton(
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
    enabled: Boolean,
    checked: Boolean,
    instanceRule: InstanceRule,
    reportInteractions: ReportInteractions,
) {
    Row(
        Modifier
            .clickable(
                enabled = enabled
            ) {
                reportInteractions.onServerRuleClicked(instanceRule)
            }
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Checkbox(
            modifier = Modifier
                .size(20.dp),
            enabled = enabled,
            checked = checked,
            onCheckedChange = { reportInteractions.onServerRuleClicked(instanceRule) }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = instanceRule.text,
            color = if (enabled) {
                Color.Unspecified
            } else {
                // same alpha as the disabled checkbox
                Color.Unspecified.copy(alpha = 0.38f)
            }
        )
    }
}

@Preview
@Composable
private fun ReportScreenPreview() {
    MoSoTheme {
        ReportScreen(
            instanceRules = listOf(
                InstanceRule(
                    1,
                    "no dummies"
                )
            ),
            selectedReportType = null,
            checkedRules = listOf(),
            additionalCommentText = "",
            reportInteractions = object : ReportInteractions {}
        )
    }
}