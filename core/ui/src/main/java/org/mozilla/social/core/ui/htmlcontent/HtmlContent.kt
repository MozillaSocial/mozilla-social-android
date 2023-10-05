package org.mozilla.social.core.ui.htmlcontent

import android.graphics.Typeface
import android.os.Build
import android.text.TextUtils
import android.widget.TextView
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
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
    maximumLineCount: Int? = null,
    textStyle: TextStyle? = null,
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

    val textColor = MoSoTheme.colors.textPrimary

    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                textStyle?.let {  textStyle ->
                    textSize = textStyle.fontSize.value
                    setTextColor(textColor.toArgb())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        typeface = textStyle.fontWeight?.let { fontWeight ->
                            Typeface.create(
                                typeface,
                                fontWeight.weight,
                                false
                            )
                        }
                    }
                }

                movementMethod = HtmlContentMovementMethod
                isClickable = false
                isLongClickable = false
                //TODO ellipsize not working
                ellipsize = TextUtils.TruncateAt.END
                maximumLineCount?.let { maxLines = it }
            }
        },
        update = {
            it.text = textContent.value
        }
    )
}