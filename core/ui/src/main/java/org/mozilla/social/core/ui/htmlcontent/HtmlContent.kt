package org.mozilla.social.core.ui.htmlcontent

import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.model.Mention

/**
 * @param mentions a list of mention objects that the htmlText contains
 * @param htmlText html String.  Must be wrapped in a <p> tag
 */
@Composable
fun HtmlContent(
    modifier: Modifier = Modifier,
    mentions: List<Mention>,
    htmlText: String,
    htmlContentInteractions: HtmlContentInteractions,
) {
    val linkColor: Color = MoSoTheme.colors.textLink
    val textContent = remember(htmlText) {
        val spannable = htmlText.htmlToSpannable(
            mentions = mentions,
            linkColor = linkColor,
            onLinkClick = htmlContentInteractions::onLinkClicked,
            onHashTagClicked = htmlContentInteractions::onHashTagClicked,
            onAccountClicked = htmlContentInteractions::onAccountClicked,
        )
        mutableStateOf(
            spannable
        )
    }

    AndroidView(
        modifier = modifier,
        factory = {
            TextView(it).apply {
                movementMethod = HtmlContentMovementMethod
                isClickable = false
                isLongClickable = false
            }
        },
        update = {
            it.text = textContent.value
        }
    )
}