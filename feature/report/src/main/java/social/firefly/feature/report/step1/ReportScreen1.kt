package social.firefly.feature.report.step1

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.core.parameter.parametersOf
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.model.InstanceRule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfCheckBox
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.animation.ExpandingAnimation
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.button.FfRadioButton
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.text.FfTextField
import social.firefly.feature.report.R
import social.firefly.feature.report.ReportDataBundle
import social.firefly.feature.report.ReportTarget
import social.firefly.feature.report.ReportType

@Composable
internal fun ReportScreen1(
    onCloseClicked: () -> Unit,
    onNextClicked: (bundle: ReportDataBundle) -> Unit,
    reportAccountId: String,
    reportAccountHandle: String,
    reportStatusId: String?,
    viewModel: ReportScreen1ViewModel =
        koinViewModel(parameters = {
            parametersOf(
                onNextClicked,
                onCloseClicked,
                reportAccountId,
                reportAccountHandle,
                reportStatusId,
            )
        }),
) {
    val instanceRules by viewModel.instanceRules.collectAsStateWithLifecycle()
    val selectedReportType by viewModel.selectedReportType.collectAsStateWithLifecycle()
    val checkedRules by viewModel.checkedRules.collectAsStateWithLifecycle()
    val additionalCommentText by viewModel.additionalCommentText.collectAsStateWithLifecycle()
    val sendToExternalServer by viewModel.sendToExternalServerChecked.collectAsStateWithLifecycle()
    ReportScreen1(
        reportTarget =
        if (reportStatusId != null) {
            ReportTarget.POST
        } else {
            ReportTarget.ACCOUNT
        },
        instanceRules = instanceRules,
        selectedReportType = selectedReportType,
        checkedRules = checkedRules,
        additionalCommentText = additionalCommentText,
        reportAccountHandle = reportAccountHandle,
        sendToExternalServer = sendToExternalServer,
        reportInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun ReportScreen1(
    reportTarget: ReportTarget,
    instanceRules: List<InstanceRule>,
    selectedReportType: ReportType?,
    checkedRules: List<InstanceRule>,
    additionalCommentText: String,
    reportAccountHandle: String,
    sendToExternalServer: Boolean,
    reportInteractions: ReportScreen1Interactions,
) {
    FfSurface {
        Column(
            modifier =
            Modifier
                .fillMaxHeight()
                .systemBarsPadding()
                .imePadding(),
        ) {
            FfCloseableTopAppBar(
                title = stringResource(id = R.string.report_screen_title),
            )
            FfDivider()
            MainContent(
                reportTarget = reportTarget,
                instanceRules = instanceRules,
                selectedReportType = selectedReportType,
                checkedRules = checkedRules,
                additionalCommentText = additionalCommentText,
                reportAccountHandle = reportAccountHandle,
                sendToExternalServer = sendToExternalServer,
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
    reportAccountHandle: String,
    sendToExternalServer: Boolean,
    reportInteractions: ReportScreen1Interactions,
) {
    Column(
        modifier =
        Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.report_prompt, "@$reportAccountHandle"),
            style = FfTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text =
            stringResource(
                id =
                if (reportTarget == ReportTarget.POST) {
                    R.string.report_instructions_for_post
                } else {
                    R.string.report_instructions_for_account
                },
            ),
            style = FfTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.choose_best_match),
            style = FfTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReportOptions(
            instanceRules = instanceRules,
            selectedReportType = selectedReportType,
            checkedRules = checkedRules,
            reportInteractions = reportInteractions,
        )

        Spacer(modifier = Modifier.height(32.dp))
        AdditionalReportFields(
            selectedReportType = selectedReportType,
            additionalCommentText = additionalCommentText,
            reportAccountHandle = reportAccountHandle,
            sendToExternalServer = sendToExternalServer,
            reportInteractions = reportInteractions,
        )

        FfButton(
            modifier =
            Modifier
                .fillMaxWidth(),
            enabled = selectedReportType != null,
            onClick = { reportInteractions.onNextClicked() },
        ) {
            Text(text = stringResource(id = R.string.next_button))
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ReportOptions(
    instanceRules: List<InstanceRule>,
    selectedReportType: ReportType?,
    checkedRules: List<InstanceRule>,
    reportInteractions: ReportScreen1Interactions,
) {
    SelectableReportType(
        reportType = ReportType.DO_NOT_LIKE,
        title = stringResource(id = R.string.report_reason_do_not_like),
        selectedReportType = selectedReportType,
        reportInteractions = reportInteractions,
    ) {
        Text(
            text = stringResource(id = R.string.report_reason_do_not_like_description),
            style = FfTheme.typography.bodyMedium,
        )
    }
    SelectableReportType(
        reportType = ReportType.SPAM,
        title = stringResource(id = R.string.report_reason_spam),
        selectedReportType = selectedReportType,
        reportInteractions = reportInteractions,
    ) {
        Text(
            text = stringResource(id = R.string.report_reason_spam_description),
            style = FfTheme.typography.bodyMedium,
        )
    }

    SelectableReportType(
        reportType = ReportType.VIOLATION,
        title = stringResource(id = R.string.report_reason_violation),
        selectedReportType = selectedReportType,
        reportInteractions = reportInteractions,
    ) {
        ExpandingAnimation(visible = selectedReportType == ReportType.VIOLATION) {
            Column {
                Text(
                    text = stringResource(id = R.string.report_reason_violation_description),
                    style = FfTheme.typography.bodyMedium,
                )
                instanceRules.forEach { instanceRule ->
                    CheckableInstanceRule(
                        checked = checkedRules.contains(instanceRule),
                        instanceRule = instanceRule,
                        reportInteractions = reportInteractions,
                    )
                }
            }
        }
    }

    SelectableReportType(
        reportType = ReportType.OTHER,
        title = stringResource(id = R.string.report_reason_other),
        selectedReportType = selectedReportType,
        reportInteractions = reportInteractions,
    ) {
        Text(
            text = stringResource(id = R.string.report_reason_other_description),
            style = FfTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SelectableReportType(
    reportType: ReportType,
    title: String,
    selectedReportType: ReportType?,
    reportInteractions: ReportScreen1Interactions,
    content: @Composable () -> Unit,
) {
    NoRipple {
        Row(
            modifier =
            Modifier
                .padding(4.dp)
                .clickable { reportInteractions.onReportTypeSelected(reportType) },
        ) {
            FfRadioButton(
                modifier =
                Modifier
                    .size(20.dp),
                selected = selectedReportType == reportType,
                onClick = { reportInteractions.onReportTypeSelected(reportType) },
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Text(
                    text = title,
                    style = FfTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W700,
                )
                content()
            }
        }
    }
}

@Composable
private fun CheckableInstanceRule(
    checked: Boolean,
    instanceRule: InstanceRule,
    reportInteractions: ReportScreen1Interactions,
) {
    Row(
        Modifier
            .clickable {
                reportInteractions.onServerRuleClicked(instanceRule)
            }
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        FfCheckBox(
            modifier =
            Modifier
                .size(20.dp),
            checked = checked,
            onCheckedChange = { reportInteractions.onServerRuleClicked(instanceRule) },
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = instanceRule.text,
        )
    }
}

@Composable
private fun AdditionalReportFields(
    selectedReportType: ReportType?,
    additionalCommentText: String,
    reportAccountHandle: String,
    sendToExternalServer: Boolean,
    reportInteractions: ReportScreen1Interactions,
) {
    // will be null if the user is on the same instance as you
    val externalInstance =
        remember(reportAccountHandle) {
            mutableStateOf(
                if (reportAccountHandle.contains("@")) {
                    reportAccountHandle.substringAfterLast("@")
                } else {
                    null
                },
            )
        }

    val showAdditionalFields =
        remember(selectedReportType) {
            mutableStateOf(
                selectedReportType != null &&
                        selectedReportType != ReportType.DO_NOT_LIKE,
            )
        }

    ExpandingAnimation(
        visible = showAdditionalFields.value,
    ) {
        Column(
            modifier = Modifier.animateContentSize(),
        ) {
            AdditionalComments(
                additionalCommentText = additionalCommentText,
                reportInteractions = reportInteractions,
            )
            Spacer(modifier = Modifier.height(16.dp))
            externalInstance.value?.let { externalInstance ->
                SendToOtherServerOption(
                    externalInstance = externalInstance,
                    checked = sendToExternalServer,
                    reportInteractions = reportInteractions,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AdditionalComments(
    additionalCommentText: String,
    reportInteractions: ReportScreen1Interactions,
) {
    Text(
        text = stringResource(id = R.string.extra_info_prompt),
        style = FfTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(16.dp))
    FfTextField(
        modifier =
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp),
        value = additionalCommentText,
        label = {
            Text(text = stringResource(id = R.string.extra_info_text_field_label))
        },
        onValueChange = { reportInteractions.onAdditionCommentTextChanged(it) },
    )
}

@Composable
private fun SendToOtherServerOption(
    externalInstance: String,
    checked: Boolean,
    reportInteractions: ReportScreen1Interactions,
) {
    Column {
        Text(
            text = stringResource(id = R.string.external_instance_title),
            style = FfTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.external_instance_description),
            style = FfTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            Modifier
                .clickable { reportInteractions.onSendToExternalServerClicked() }
                .padding(4.dp)
                .fillMaxWidth(),
        ) {
            FfCheckBox(
                modifier =
                Modifier
                    .size(20.dp),
                checked = checked,
                onCheckedChange = { reportInteractions.onSendToExternalServerClicked() },
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.external_instance_option, externalInstance),
                style = FfTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun ReportScreenPreview() {
    val noDummies =
        InstanceRule(
            1,
            "no dummies",
        )
    val noDogs =
        InstanceRule(
            2,
            "no dogs",
        )
    FfTheme {
        ReportScreen1(
            reportTarget = ReportTarget.POST,
            instanceRules =
            listOf(
                noDummies,
                noDogs,
            ),
            selectedReportType = ReportType.VIOLATION,
            checkedRules =
            listOf(
                noDogs,
            ),
            additionalCommentText = "",
            reportAccountHandle = "john@firefly.com",
            sendToExternalServer = false,
            reportInteractions = object : ReportScreen1Interactions {},
        )
    }
}

@Preview
@Composable
private fun ReportScreenPreviewDarkMode() {
    val noDummies =
        InstanceRule(
            1,
            "no dummies",
        )
    val noDogs =
        InstanceRule(
            2,
            "no dogs",
        )
    KoinApplication(application = {
        modules(navigationModule)
    }) {
        FfTheme(
            darkTheme = true,
        ) {
            ReportScreen1(
                reportTarget = ReportTarget.POST,
                instanceRules =
                listOf(
                    noDummies,
                    noDogs,
                ),
                selectedReportType = ReportType.VIOLATION,
                checkedRules =
                listOf(
                    noDogs,
                ),
                additionalCommentText = "",
                reportAccountHandle = "john@firefly.com",
                sendToExternalServer = false,
                reportInteractions = object : ReportScreen1Interactions {},
            )
        }
    }
}
