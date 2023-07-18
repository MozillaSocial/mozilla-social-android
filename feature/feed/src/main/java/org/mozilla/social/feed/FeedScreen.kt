package org.mozilla.social.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.model.Account
import org.mozilla.social.model.Page
import org.mozilla.social.model.Status

@Composable
fun FeedScreen(viewModel: FeedViewModel = koinViewModel<FeedViewModel>()) {
    FeedScreen(publicTimeline = viewModel.feed.collectAsState(null).value)
}

@Composable
fun FeedScreen(publicTimeline: Page<List<Status>>?) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        publicTimeline?.contents?.forEach { status ->
            StatusCard(status = status)
        }
    }
}

@Composable
fun StatusCard(status: Status) {
    Card(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val spannedText = HtmlCompat.fromHtml(status.content, 0)

        Column(Modifier.padding(4.dp).fillMaxSize()) {
            Text(text = status.account.username)
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                factory = { MaterialTextView(it) },
                update = { it.text = spannedText }
            )
        }
    }
}


@Preview
@Composable
fun FeedScreenPreview() {
    MozillaSocialTheme {
        FeedScreen(
            publicTimeline = Page(
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