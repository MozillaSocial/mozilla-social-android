package org.mozilla.social.feature.thread

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.postcard.PostCardList

@Composable
fun ThreadScreen(
    threadStatusId: String,
    onPostClicked: (String) -> Unit,
    onReplyClicked: (String) -> Unit,
    viewModel: ThreadViewModel = koinViewModel(parameters = { parametersOf(
        threadStatusId,
        onPostClicked,
        onReplyClicked
    ) })
) {
    PostCardList(
        items = viewModel.statuses.collectAsState(emptyList()).value,
        postCardInteractions = viewModel.postCardDelegate
    )
}