package social.firefly.feature.report.step2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import social.firefly.common.Resource
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.designsystem.utils.NoRipple
import social.firefly.core.model.InstanceRule
import social.firefly.core.ui.common.FfCheckBox
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.loading.FfCircularProgressIndicator
import social.firefly.feature.report.R
import social.firefly.feature.report.ReportDataBundle
import social.firefly.feature.report.ReportType

@Composable
internal fun ReportScreen2(
    onCloseClicked: () -> Unit,
    onReportSubmitted: (bundle: ReportDataBundle.ReportDataBundleForScreen3) -> Unit,
    reportAccountId: String,
    reportAccountHandle: String,
    reportStatusId: String?,
    reportType: ReportType,
    checkedInstanceRules: List<InstanceRule>,
    additionalText: String,
    sendToExternalServer: Boolean,
    viewModel: ReportScreen2ViewModel =
        koinViewModel(parameters = {
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
        }),
) {
    val uiState by viewModel.statuses.collectAsStateWithLifecycle()
    val reportIsSending by viewModel.reportIsSending.collectAsStateWithLifecycle()
    ReportScreen2(
        reportAccountHandle = reportAccountHandle,
        uiState = uiState,
        reportIsSending = reportIsSending,
        hasPreAttachedStatus = reportStatusId != null,
        reportInteractions = viewModel,
    )
}

@Composable
private fun ReportScreen2(
    reportAccountHandle: String,
    uiState: Resource<List<ReportStatusUiState>>,
    reportIsSending: Boolean,
    hasPreAttachedStatus: Boolean,
    reportInteractions: ReportScreen2Interactions,
) {
    FfSurface {
        Column(
            modifier =
            Modifier
                .fillMaxHeight()
                .systemBarsPadding(),
        ) {
            FfCloseableTopAppBar(
                title = stringResource(id = R.string.report_screen_title),
            )

            TopContent(
                reportAccountHandle = reportAccountHandle,
                hasPreAttachedStatus = hasPreAttachedStatus,
            )

            FfDivider()

            MiddleContent(
                modifier = Modifier.weight(1f),
                uiState = uiState,
                reportInteractions = reportInteractions,
            )

            FfDivider()

            BottomContent(
                reportIsSending = reportIsSending,
                reportInteractions = reportInteractions,
            )
        }
    }
}

@Composable
private fun TopContent(
    reportAccountHandle: String,
    hasPreAttachedStatus: Boolean,
) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.report_prompt, "@$reportAccountHandle"),
            style = FfTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text =
            stringResource(
                id =
                if (hasPreAttachedStatus) {
                    R.string.screen_2_prompt_with_attached_status
                } else {
                    R.string.screen_2_prompt
                },
            ),
            style = FfTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun MiddleContent(
    modifier: Modifier = Modifier,
    uiState: Resource<List<ReportStatusUiState>>,
    reportInteractions: ReportScreen2Interactions,
) {
    Box(
        modifier = modifier,
    ) {
        when (uiState) {
            is Resource.Loading -> {
                MaxSizeLoading()
            }

            is Resource.Error -> {
                GenericError(
                    onRetryClicked = { reportInteractions.onRetryClicked() },
                )
            }

            is Resource.Loaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    item {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                            text = stringResource(id = R.string.select_all_that_apply),
                        )
                    }
                    items(
                        count = uiState.data.count(),
                        key = { uiState.data[it].statusId },
                    ) { index ->
                        val item = uiState.data[index]
                        SelectableStatusCard(
                            uiState = item,
                            reportInteractions = reportInteractions,
                        )
                        if (index < uiState.data.count() - 1) {
                            FfDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomContent(
    reportIsSending: Boolean,
    reportInteractions: ReportScreen2Interactions,
) {
    FfButton(
        modifier =
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        onClick = { reportInteractions.onReportClicked() },
        enabled = !reportIsSending,
    ) {
        if (reportIsSending) {
            FfCircularProgressIndicator(
                modifier = Modifier.size(24.dp),
            )
        } else {
            Text(text = stringResource(id = R.string.submit_report_button))
        }
    }
}

@Composable
private fun SelectableStatusCard(
    uiState: ReportStatusUiState,
    reportInteractions: ReportScreen2Interactions,
) {
    val context = LocalContext.current

    NoRipple {
        Row(
            modifier =
            Modifier
                .padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                .clickable { reportInteractions.onStatusClicked(uiState.statusId) },
        ) {
            FfCheckBox(
                modifier = Modifier.align(Alignment.CenterVertically),
                checked = uiState.checked,
                onCheckedChange = { reportInteractions.onStatusClicked(uiState.statusId) },
            )

            AsyncImage(
                modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(FfTheme.colors.layer2)
                    .align(Alignment.CenterVertically),
                model = uiState.avatarUrl,
                contentDescription = "",
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = uiState.userName,
                            style = FfTheme.typography.labelMedium,
                            fontWeight = FontWeight.W600,
                        )
                        Text(
                            text = "@${uiState.handle}",
                            style = FfTheme.typography.bodySmall,
                            color = FfTheme.colors.textSecondary,
                        )
                    }
                    Text(
                        text = uiState.postTimeSince.build(context),
                        style = FfTheme.typography.bodySmall,
                        color = FfTheme.colors.textSecondary,
                    )
                }

                social.firefly.core.ui.htmlcontent.HtmlContent(
                    mentions = emptyList(),
                    htmlText = uiState.htmlStatusText,
                    htmlContentInteractions =
                    object :
                        social.firefly.core.ui.htmlcontent.HtmlContentInteractions {},
                    maximumLineCount = 2,
                    clickableLinks = false,
                )
            }
        }
    }
}
