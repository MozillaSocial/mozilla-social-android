package org.mozilla.social.core.ui.postcontent

import android.text.Selection
import android.text.Spannable
import android.text.method.BaseMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * The object is meant to be used in [TextView.setMovementMethod].  It allows [ClickableSpan]s
 * to be clicked.  Use this instead of [LinkMovementMethod] because [LinkMovementMethod] will
 * intercept all touch events, even if you don't touch a link.  This allows parent layouts to
 * receive clicks if you don't click a link.
 *
 * Based on this stack overflow answer https://stackoverflow.com/a/34200772/5321814
 *
 * I edited the kdoc and converted the class to kotlin.
 *
 * License for the stack overflow post: https://creativecommons.org/licenses/by-sa/3.0/legalcode
 */
object ClickableMovementMethod : BaseMovementMethod() {
    override fun canSelectArbitrarily(): Boolean {
        return false
    }

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val links: Array<ClickableSpan>? = if (y < 0 || y > layout.height) {
                null
            } else {
                val line = layout.getLineForVertical(y)
                if (x < layout.getLineLeft(line) || x > layout.getLineRight(line)) {
                    null
                } else {
                    val off = layout.getOffsetForHorizontal(line, x.toFloat())
                    buffer.getSpans(off, off, ClickableSpan::class.java)
                }
            }
            if (!links.isNullOrEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    links[0].onClick(widget)
                } else {
                    Selection.setSelection(
                        buffer, buffer.getSpanStart(links[0]),
                        buffer.getSpanEnd(links[0])
                    )
                }
                return true
            } else {
                Selection.removeSelection(buffer)
            }
        }
        return false
    }

    override fun initialize(widget: TextView, text: Spannable) {
        Selection.removeSelection(text)
    }
}