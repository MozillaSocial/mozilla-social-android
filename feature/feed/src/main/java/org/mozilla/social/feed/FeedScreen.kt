package org.mozilla.social.feed

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.postcard.PostCardList

@Composable
fun FeedScreen(
    onPostClicked: (String) -> Unit,
    onReplyClicked: (String) -> Unit,
    onReportClicked: (accountId: String, statusId: String) -> Unit,
    viewModel: FeedViewModel = koinViewModel(parameters = { parametersOf(
        onPostClicked,
        onReplyClicked,
        onReportClicked,
    ) })
) {
    PostCardList(
        feed = viewModel.feed,
        postCardInteractions = viewModel.postCardDelegate,
        reccs = viewModel.reccs.collectAsState(initial = null).value,
    )

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.postCardDelegate.errorToastMessage.collect {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}
