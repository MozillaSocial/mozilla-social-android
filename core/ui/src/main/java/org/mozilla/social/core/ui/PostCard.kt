package org.mozilla.social.core.ui

import android.text.Spanned
import android.text.SpannedString
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.google.android.material.textview.MaterialTextView
import org.mozilla.social.core.designsystem.theme.FirefoxColor
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.model.entity.Status


@Composable
fun PostCard(status: Status) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Text(text = status.account.username)
        if (status.content.isEmpty()) {
            if (status.boostedStatus?.content?.isNotEmpty() == true) {
                BoostedPost(status = status)
            }
        } else {
            OriginalPost(status = status)
        }
        }
    }
}

@Composable
fun OriginalPost(status: Status) {
    val spannedText = HtmlCompat.fromHtml(status.content, 0)
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp)
            .wrapContentHeight(),
        factory = { MaterialTextView(it) },
        update = { it.text = spannedText }
    )
    status.mediaAttachments.forEach {
        AsyncImage(
            model = it.previewUrl,
            contentDescription = it.description
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
@Composable
fun BoostedPost(status: Status) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = FirefoxColor.LightGrey05
        )
    ) {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            status.boostedStatus?.let {
                Text(text = it.account.username)
                OriginalPost(status = it)
            }
        }
    }
}

@Preview
@Composable
fun PostCardPreview() {
    MozillaSocialTheme {
//        PostCard(
//            status =
//                Status(
//                    "1",
//                    "asdf",
//                     Account("1", username = "username"),
//                    content = "here's a post",
//                    isSensitive = false,
//                )
//            )
    }
}