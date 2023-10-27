package org.mozilla.social.core.ui.htmlcontent

/**
 * Html content returned from mastodon has some html spans that can be removed to reduce the
 * visual size of html links.  The removes those spans and adds an ellipsis to others.  See the
 * unit test for an example of what the html might look like.
 */
fun String.reduceHtmlLinks(): String {
    var reducedHtml = this

    do {
        val spanStartIndex = reducedHtml.indexOf(INVISIBLE_SPAN)
        if (spanStartIndex == -1) break
        val endIndex = reducedHtml.indexOf(SPAN_END, spanStartIndex)
        reducedHtml = reducedHtml.replace(reducedHtml.substring(spanStartIndex, endIndex + SPAN_END.length), "")
    } while (true)

    var loopStartIndex = 0
    do {
        val spanStartIndex = reducedHtml.indexOf(ELLIPSIS_SPAN, startIndex = loopStartIndex)
        if (spanStartIndex == -1) break
        val endIndex = reducedHtml.indexOf(SPAN_END, spanStartIndex)
        reducedHtml = StringBuilder(reducedHtml).apply {
            insert(endIndex, "…")
        }.toString()
        loopStartIndex = endIndex
    } while (true)
    return reducedHtml
}

private const val INVISIBLE_SPAN = "<span class=\"invisible\">"
private const val SPAN_END = "</span>"
private const val ELLIPSIS_SPAN = "<span class=\"ellipsis\">"