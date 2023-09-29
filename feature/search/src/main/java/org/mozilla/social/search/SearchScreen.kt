@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {

}

@Preview
@Composable
fun FeedScreenPreview() {
    MoSoTheme {
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