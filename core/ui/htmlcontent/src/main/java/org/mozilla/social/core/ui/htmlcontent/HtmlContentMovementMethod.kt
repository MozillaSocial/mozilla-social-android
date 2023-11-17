package org.mozilla.social.core.ui.htmlcontent

import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * The object is meant to be used in [TextView.setMovementMethod].  It allows [ClickableSpan]s
 * to be clicked.  Use this instead of [LinkMovementMethod] because [LinkMovementMethod] will
 * intercept all touch events, even if you don't touch a link.  This allows parent layouts to
 * receive clicks if you don't click a link.
 *
 */
object HtmlContentMovementMethod : LinkMovementMethod() {
    private var isTouching = false

    override fun onTouchEvent(
        widget: TextView,
        buffer: Spannable,
        event: MotionEvent,
    ): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_DOWN ||
            action == MotionEvent.ACTION_CANCEL
        ) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout: Layout = widget.layout
            val line: Int = layout.getLineForVertical(y)
            val off: Int = layout.getOffsetForHorizontal(line, x.toFloat())

            // ClickableSpan workaround
            val links =
                buffer.getSpans(
                    off,
                    off,
                    ClickableSpan::class.java,
                )
            return if (links.isNotEmpty()) {
                val link = links[0]
                if (action == MotionEvent.ACTION_UP) {
                    link.onClick(widget)
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(
                        buffer,
                        buffer.getSpanStart(link),
                        buffer.getSpanEnd(link),
                    )
                }
                isTouching = true
                true
            } else {
                Selection.removeSelection(buffer)
                false
            }
        }
        return Touch.onTouchEvent(widget, buffer, event)
    }
}
