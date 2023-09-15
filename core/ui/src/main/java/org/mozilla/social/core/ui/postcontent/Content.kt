package org.mozilla.social.core.ui.postcontent

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.social.common.utils.DimenUtil
import org.mozilla.social.model.Mention

@Composable
fun PostContent(
    mentions: List<Mention>,
    htmlText: String,
    postContentInteractions: PostContentInteractions,
) {
    val context = LocalContext.current
    val linkColor: Color = MaterialTheme.colorScheme.primary
    val textContent = remember(htmlText) {
        mutableStateOf(
            htmlText.htmlToSpannable(
                mentions = mentions,
                linkColor = linkColor,
                onLinkClick = postContentInteractions::onLinkClicked,
                onHashTagClicked = postContentInteractions::onHashTagClicked,
                onAccountClicked = postContentInteractions::onAccountClicked,
            )
        )
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp),
        factory = {
            TextView(it).apply {
                // there is an extra chunk of padding added, so lets remove some of that
                setPadding(0, 0, 0, DimenUtil.dpToPxInt(context, -20f))
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { it.text = textContent.value }
    )
}