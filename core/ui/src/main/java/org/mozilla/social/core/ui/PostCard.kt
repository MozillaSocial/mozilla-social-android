package org.mozilla.social.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.google.android.material.textview.MaterialTextView
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.model.Account
import org.mozilla.social.model.Page
import org.mozilla.social.model.Status


@Composable
fun PostCard(status: Status) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val spannedText = HtmlCompat.fromHtml(status.content, 0)

        Column(
            Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Text(text = status.account.username)
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp)
                    .wrapContentHeight(),
                factory = { MaterialTextView(it) },
                update = { it.text = spannedText }
            )
        }
    }
}

@Preview
@Composable
fun PostCardPreview() {
    MozillaSocialTheme {
        PostCard(
            status =
                Status(
                    "1",
                    "asdf",
                    account = Account("1", username = "username"),
                    content = "here's a post"
                )
            )
    }
}