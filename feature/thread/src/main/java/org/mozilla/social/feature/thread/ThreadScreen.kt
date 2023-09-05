package org.mozilla.social.feature.thread

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.postcard.PostCardList

@Composable
fun ThreadScreen(
    threadStatusId: String,
    onPostClicked: (String) -> Unit,
    onReplyClicked: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onReportClicked: (accountId: String, statusId: String) -> Unit,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(
        threadStatusId,
        onPostClicked,
        onReplyClicked,
        onReportClicked,
    ) })
) {
    Column {
        TopBar(onCloseClicked)
        Divider()

        PostCardList(
            items = viewModel.statuses.collectAsState(emptyList()).value,
            postCardInteractions = viewModel.postCardDelegate
        )
    }
}

@Composable
private fun TopBar(
    onCloseClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { onCloseClicked() },
        ) {
            Icon(
                Icons.Default.Close,
                "close",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "Thread",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}