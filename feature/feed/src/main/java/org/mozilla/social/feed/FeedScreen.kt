package org.mozilla.social.feed

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.ui.postcard.PostCardList

@Composable
fun FeedScreen(
    onReplyClicked: (String) -> Unit,
    viewModel: FeedViewModel = koinViewModel(parameters = { parametersOf(onReplyClicked) })
) {
    PostCardList(
        feed = viewModel.feed,
        postCardInteractions = viewModel
    )
}
