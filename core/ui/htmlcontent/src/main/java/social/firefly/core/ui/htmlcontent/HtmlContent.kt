package social.firefly.core.ui.htmlcontent

import android.graphics.Typeface
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnNextLayout
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.model.Mention
import kotlin.math.min

/**
 * @param mentions a list of mention objects that the htmlText contains
 * @param htmlText html String.  Must be wrapped in a <p> tag
 */
@Composable
fun HtmlContent(
    modifier: Modifier = Modifier,
    mentions: List<Mention> = emptyList(),
    htmlText: String,
    htmlContentInteractions: HtmlContentInteractions,
    maximumLineCount: Int = Int.MAX_VALUE,
    textStyle: TextStyle = FfTheme.typography.bodyMedium,
    textColor: Color = FfTheme.colors.textPrimary,
    linkColor: Color = FfTheme.colors.textLink,
    clickableLinks: Boolean = true,
) {
    val textContent =
        remember(htmlText) {
            val spannable =
                htmlText.reduceHtmlLinks().htmlToSpannable(
                    mentions = mentions,
                    linkColor = linkColor,
                    onLinkClick = htmlContentInteractions::onLinkClicked,
                    onHashTagClicked = htmlContentInteractions::onHashTagClicked,
                    onAccountClicked = htmlContentInteractions::onAccountClicked,
                )
            mutableStateOf(
                spannable,
            )
        }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                textSize = textStyle.fontSize.value
                setTextColor(textColor.toArgb())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    typeface =
                        textStyle.fontWeight?.let { fontWeight ->
                            Typeface.create(
                                typeface,
                                fontWeight.weight,
                                false,
                            )
                        }
                }

                if (clickableLinks) {
                    movementMethod = HtmlContentMovementMethod
                }
                isClickable = false
                isLongClickable = false
                maxLines = maximumLineCount
            }
        },
        update = { textView ->
            textView.text = textContent.value
            textView.maxLines = maximumLineCount

            // Add ellipsize manually
            // setting textView.ellipsize = TextUtils.TruncateAt.END doesn't seem to work.
            textView.doOnNextLayout {
                textView.layout?.let { layout ->
                    val textViewLineCount = textView.lineCount
                    if (textViewLineCount > maximumLineCount) {
                        val indexOfLastChar = layout.getLineEnd(maximumLineCount - 1)
                        val indexToEndAt = indexOfLastChar - min(3, indexOfLastChar)
                        val spanned = textView.text.subSequence(0, indexToEndAt).trim() as? Spanned
                        textView.text =
                            SpannableStringBuilder()
                                .append(spanned)
                                .append("…")
                    }
                }
            }
        },
    )
}
