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

    /**
     * @param quantity determines which plural to use
     * @param formatArgs the value inserted into the string
     */
    private class QuantityStringResource(
        @param:PluralsRes val resId: Int,
        val quantity: Int,
        vararg val formatArgs: Any,
    ) : StringFactory

    fun build(context: Context): String = when (this) {
        is LiteralString -> literalValue
        is StringResource -> context.getString(resId)
        is FormattedStringResource -> context.getString(resId, *formatArgs)
        is QuantityStringResource -> context.resources.getQuantityString(
            resId,
            quantity,
            *formatArgs,
        )
    }

    companion object {
        fun string(@StringRes resId: Int): StringFactory = StringResource(resId)

        fun string(
            @StringRes resId: Int,
            vararg formatArgs: Any,
        ): StringFactory = FormattedStringResource(resId, *formatArgs)

        /**
         * @param quantity determines which plural to use
         * @param formatArgs the value inserted into the string
         */
        fun quantityString(
            @PluralsRes resId: Int,
            quantity: Int,
            vararg formatArgs: Any,
        ): StringFactory = QuantityStringResource(resId, quantity, *formatArgs)

        fun literalString(
            literalValue: String
        ): StringFactory = LiteralString(literalValue)
    }
}

/**
 * Pass strings or string factories into the constructor, then build to concatenate the values.
 *
 * Useful if you have multiple string factories / strings that need to be
 * concatenated, but you don't have access to context just yet.
 */
class StringFactoryConcatenator(
    private vararg val stringArgs: Any,
) {
    fun build(context: Context): String =
        buildString {
            stringArgs.forEach {
                when (it) {
                    is StringFactoryConcatenator -> append(it.build(context))
                    is StringFactory -> append(it.build(context))
                    else -> append(it)
                }
            }
        }
}