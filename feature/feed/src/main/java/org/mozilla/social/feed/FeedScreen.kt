@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.ui.postcard.PostCard
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.model.entity.Status

@Composable
fun FeedScreen(
    onReplyClicked: (String) -> Unit,
    viewModel: FeedViewModel = koinViewModel(parameters = { parametersOf(onReplyClicked) })
) {
    FeedScreen(
        publicTimeline = viewModel.statusFeed.collectAsState(initial = null).value,
        postCardInteractions = viewModel,
    )
}

@Composable
fun FeedScreen(
    publicTimeline: List<Status>?,
    postCardInteractions: PostCardInteractions,
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        publicTimeline?.let { statuses ->
            items(statuses.size) { index ->
                PostCard(status = statuses[index], postCardInteractions)
                if (index < statuses.lastIndex) {
                    Divider()
                }
            }
        }
    }
}

@Preview
@Composable
fun FeedScreenPreview() {
    MozillaSocialTheme {
//        FeedScreen(
//            publicTimeline = Page(
//                contents = listOf(
//                    Status(
//                        "1",
//                        "asdf",
//                        account = Account("1", username = "username"),
//                        content = "here's a post",
//                        isSensitive = false,
//                        mediaAttachments = listOf()
//                    )
//                )
//            ),
//            onNewPostClicked = {}
//        )
    }
}