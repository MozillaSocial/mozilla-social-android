package org.mozilla.social.core.ui.spans

import android.text.Spannable
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.mozilla.social.model.Mention

fun Spannable.makeLinksClickable(
    mentions: List<Mention>,
    linkColor: Color,
    onLinkClick: (url: String) -> Unit,
    onHashTagClicked: (hashTag: String) -> Unit,
    onAccountClicked: (accountName: String) -> Unit,
): Spannable {
    val urlSpans = getSpans(0, length, URLSpan::class.java)
    urlSpans.forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        val flags = getSpanFlags(span)
        val spanText = substring(start, end)
        setSpan(
            object: URLSpan(span.url) {
                override fun onClick(view: View) {
                    println("johnny text: $spanText")
                    println("johnny url: $url")
                    when {
                        spanText.startsWith("@") -> {
                            mentions.find {
                                it.acct == spanText.substringAfter("@")
                            }?.accountId?.let { onAccountClicked(it) }
                        }
                        spanText.startsWith("#") -> {
                            onHashTagClicked(spanText.substringAfter("#"))
                        }
                        else -> onLinkClick(url)
                    }
                }
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = linkColor.toArgb()
                }
            },
            start,
            end,
            flags,
        )
        removeSpan(span)
    }
    return this
}