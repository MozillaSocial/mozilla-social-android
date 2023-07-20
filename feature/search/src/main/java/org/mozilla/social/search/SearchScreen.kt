@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.ui.PostCard
import org.mozilla.social.feature.search.R
import org.mozilla.social.model.Account
import org.mozilla.social.model.Page
import org.mozilla.social.model.Status

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {
    LocalFeedScreen(
        localTimeline = viewModel.publicFeed.collectAsState(null).value,
    )
}

@Composable
fun LocalFeedScreen(
    localTimeline: Page<List<Status>>?,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.local_feed),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                localTimeline?.contents?.forEach { status ->
                    PostCard(status = status)
                }
            }
        }
    }
}

@Preview
@Composable
fun FeedScreenPreview() {
    MozillaSocialTheme {
        LocalFeedScreen(
            localTimeline = Page(
                contents = listOf(
                    Status(
                        "1",
                        "asdf",
                        account = Account("1", username = "username"),
                        content = "here's a post"
                    )
                )
            )
        )
    }
}