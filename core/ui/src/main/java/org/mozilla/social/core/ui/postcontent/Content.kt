package org.mozilla.social.core.ui.postcontent

import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.social.model.Mention

/**
 * @param mentions a list of mention objects that the htmlText contains
 * @param htmlText html String.  Must be wrapped in a <p> tag
 */
@Composable
fun PostContent(
    modifier: Modifier = Modifier,
    mentions: List<Mention>,
    htmlText: String,
    postContentInteractions: PostContentInteractions,
) {
    val linkColor: Color = MaterialTheme.colorScheme.primary
    val textContent = remember(htmlText) {
        val spannable = htmlText.htmlToSpannable(
            mentions = mentions,
            linkColor = linkColor,
            onLinkClick = postContentInteractions::onLinkClicked,
            onHashTagClicked = postContentInteractions::onHashTagClicked,
            onAccountClicked = postContentInteractions::onAccountClicked,
        )
        mutableStateOf(
            spannable
        )
    }

    AndroidView(
        modifier = modifier,
        factory = {
            TextView(it).apply {
                movementMethod = ClickableMovementMethod
                isClickable = false
                isLongClickable = false
            }
        },
        update = {
            it.text = textContent.value
        }
    )
}