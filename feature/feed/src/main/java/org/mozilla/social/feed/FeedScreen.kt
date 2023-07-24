@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.core.ui.PostCard
import org.mozilla.social.model.Page
import org.mozilla.social.model.entity.Status

@Composable
fun FeedScreen(
    onNewPostClicked: () -> Unit,
    viewModel: FeedViewModel = koinViewModel()
) {
    FeedScreen(
        publicTimeline = viewModel.feed.collectAsState(initial = null).value,
        onNewPostClicked = onNewPostClicked,
    )
}

@Composable
fun FeedScreen(
    publicTimeline: List<Status>?,
    onNewPostClicked: () -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            MozillaAppBar(scrollBehavior = scrollBehavior)
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(onClick = { onNewPostClicked() }) {
                Icon(Icons.Rounded.Add, "new post")
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                publicTimeline?.forEach { status ->
                    PostCard(status = status)
                }
            }
        }
    }
}

// TODO@DA move to core:ui module
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MozillaAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Mozilla Social",
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        },
        // leaving this since we might want to add something later
//        navigationIcon = {
//            IconButton(onClick = {}) {
//                Icon(
//                    Icons.Rounded.ExitToApp, "new post",
//                    tint = MaterialTheme.colorScheme.inverseOnSurface,
//                )
//            }
//        },
//        actions = {
//            IconButton(onClick = {}) {
//                Icon(
//                    Icons.Rounded.ExitToApp, "new post",
//                    tint = MaterialTheme.colorScheme.inverseOnSurface,
//                )
//            }
//        },
        colors = colors,
        modifier = modifier.testTag("mozillaSocialAppBar"),
        scrollBehavior = scrollBehavior,
    )

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