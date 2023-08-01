@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.ui.PostCard
import org.mozilla.social.model.Status

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    LocalFeedScreen(
        localTimeline = viewModel.feed.collectAsState(initial = null).value,
    )
}

@Composable
fun LocalFeedScreen(
    localTimeline: List<Status>?,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        localTimeline?.forEach { status ->
            PostCard(status = status)
        }
    }
}

@Preview
@Composable
fun FeedScreenPreview() {
    MozillaSocialTheme {
//        LocalFeedScreen(
//            localTimeline = Page(
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
//            )
//        )
    }
}