package org.mozilla.social.common.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

/**
 * String wrapper class that supports literals and string resources
 */
sealed interface StringFactory {
    private data class LiteralString(
        val literalValue: String,
    ) : StringFactory

    private data class StringResource(
        @param:StringRes val resId: Int,
    ) : StringFactory

    private class FormattedStringResource(
        @param:StringRes val resId: Int,
        vararg val formatArgs: Any,
    ) : StringFactory

    private class QuantityStringResource(
        @param:PluralsRes val resId: Int,
        val quantity: Int,
    ) : StringFactory

    fun build(context: Context): String = when (this) {
        is LiteralString -> literalValue
        is StringResource -> context.getString(resId)
        is FormattedStringResource -> context.getString(resId, formatArgs)
        is QuantityStringResource -> context.resources.getQuantityString(
            resId,
            quantity, // determines which plural to use
            quantity, // the value inserted into the string
        )
    }

    companion object {
        fun string(@StringRes resId: Int): StringFactory = StringResource(resId)

        fun string(
            @StringRes resId: Int,
            vararg formatArgs: Any,
        ): StringFactory = FormattedStringResource(resId, formatArgs)

        fun quantityString(
            @PluralsRes resId: Int,
            count: Int,
        ): StringFactory = QuantityStringResource(resId, count)

        fun literalString(
            literalValue: String
        ): StringFactory = LiteralString(literalValue)
    }
}