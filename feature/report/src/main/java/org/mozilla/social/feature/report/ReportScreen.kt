package org.mozilla.social.feature.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
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
        reportInteractions = viewModel
    )
}

@Composable
private fun ReportScreen(
    instanceRules: List<InstanceRule>,
    selectedReportType: ReportType?,
    reportInteractions: ReportInteractions,
) {
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        TopBar(
            reportInteractions = reportInteractions,
        )
        Divider()
        MainContent(
            instanceRules = instanceRules,
            selectedReportType = selectedReportType,
            reportInteractions = reportInteractions,
        )
    }
}

@Composable
private fun TopBar(
    reportInteractions: ReportInteractions,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { reportInteractions.onCloseClicked() },
        ) {
            Icon(
                Icons.Rounded.Close,
                "close",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "Report",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun MainContent(
    instanceRules: List<InstanceRule>,
    selectedReportType: ReportType?,
    reportInteractions: ReportInteractions,
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = "Tell us what's wrong with this post",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = "Choose the best match:")
        SelectableReportType(
            reportType = ReportType.SPAM,
            title = "It's spam",
            selectedReportType = selectedReportType,
            reportInteractions = reportInteractions,
        ) {
            Text(text = "Malicious links, fake engagement, or repetitive replies")
        }

        SelectableReportType(
            reportType = ReportType.VIOLATION,
            title = "It violates one or more of the server rules",
            selectedReportType = selectedReportType,
            reportInteractions = reportInteractions,
        ) {
            LazyColumn {
                items(
                    count = instanceRules.size,
                    key = { index -> instanceRules[index].id }
                ) { index ->
                    CheckableInstanceRule(
                        instanceRule = instanceRules[index],
                        reportInteractions = reportInteractions,
                    )
                }
            }
        }

        SelectableReportType(
            reportType = ReportType.OTHER,
            title = "It's something else",
            selectedReportType = selectedReportType,
            reportInteractions = reportInteractions,
        ) {
            Text(text = "The issue does not fit into other categories")
        }
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
        modifier = Modifier.padding(4.dp)
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
                text = title
            )
            content()
        }

    }
}

@Composable
private fun CheckableInstanceRule(
    instanceRule: InstanceRule,
    reportInteractions: ReportInteractions,
) {

}

@Preview
@Composable
private fun ReportScreenPreview() {
    ReportScreen(
        instanceRules = listOf(
            InstanceRule(
                1,
                "no dummies"
            )
        ),
        selectedReportType = null,
        reportInteractions = object : ReportInteractions {}
    )
}