package org.mozilla.social.core.ui.htmlcontent

import android.text.Spannable
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import org.mozilla.social.core.model.Mention

fun String.htmlToSpannable(
    mentions: List<Mention>,
    linkColor: Color,
    onLinkClick: (url: String) -> Unit,
    onHashTagClicked: (hashTag: String) -> Unit,
    onAccountClicked: (accountName: String) -> Unit,
): Spannable {
    // the html must be wrapped in a <p> tag in order for it to be parsed by HtmlCompat.fromHtml
    val html = if (!startsWith("<p>")) {
        "<p>$this</p>"
    } else {
        this
    }
    return HtmlCompat.fromHtml(html, 0)
        .trim('\n')
        .toSpannable()
        .apply {
            val urlSpans = getSpans(0, length, URLSpan::class.java)
            urlSpans.forEach { span ->
                val start = getSpanStart(span)
                val end = getSpanEnd(span)
                val flags = getSpanFlags(span)
                val spanText = substring(start, end)
                setSpan(
                    object: URLSpan(span.url) {
                        override fun onClick(view: View) {
                            when {
                                spanText.startsWith("@") -> {
                                    // find the correct mention
                                    mentions.find {
                                        // check the user name and the domain
                                        it.acct.substringBeforeLast("@") == spanText.substringAfter("@")
                                                && url.contains(it.acct.substringAfter("@"))
                                    }?.accountId?.let {
                                        onAccountClicked(it)
                                    }
                                }
                                spanText.startsWith("#") ->
                                    onHashTagClicked(spanText.substringAfter("#"))
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
        }
}