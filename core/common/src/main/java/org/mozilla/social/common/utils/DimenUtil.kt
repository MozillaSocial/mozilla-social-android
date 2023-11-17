package org.mozilla.social.common.utils

import android.content.Context
import android.util.TypedValue

object DimenUtil {
    @JvmStatic
    fun dpToPx(
        context: Context,
        dp: Float,
    ): Float =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics,
        )

    @JvmStatic
    fun dpToPxInt(
        context: Context,
        dp: Float,
    ): Int = dpToPx(context, dp).toInt()

    // use convertDpToPixel to get the density ratio.  Using context.resources.displayMetrics.density
    // explicitly could give an incorrect value on some devices because of custom scaling built into
    // some vendors' OS
    @JvmStatic
    fun pxToDp(
        context: Context,
        px: Float,
    ): Float = px / dpToPx(context, 1F)

    @JvmStatic
    fun pxToDpInt(
        context: Context,
        px: Float,
    ): Int = pxToDp(context, px).toInt()
}

fun Float.toPx(context: Context): Float = DimenUtil.dpToPx(context, this)

fun Float.toDp(context: Context): Float = DimenUtil.pxToDp(context, this)

fun Float.toPxInt(context: Context): Int = DimenUtil.dpToPxInt(context, this)

fun Float.toDpInt(context: Context): Int = DimenUtil.pxToDpInt(context, this)
