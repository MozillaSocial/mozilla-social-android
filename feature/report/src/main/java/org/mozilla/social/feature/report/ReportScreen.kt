package org.mozilla.social.feature.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
        selectedReportType = viewModel.selectedReportType.collectAsState().value,
        reportInteractions = viewModel
    )
}

@Composable
private fun ReportScreen(
    selectedReportType: ReportType?,
    reportInteractions: ReportInteractions,
) {
    Column {
        TopBar(
            reportInteractions = reportInteractions,
        )
        Divider()
        MainScreen(
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
private fun MainScreen(
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

    }
}

@Preview
@Composable
private fun ReportScreenPreview() {
    ReportScreen(
        selectedReportType = null,
        reportInteractions = object : ReportInteractions {}
    )
}