package org.mozilla.social.common.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

/**
 * String wrapper class that supports literals and string resources
 */
sealed interface StringFactory {
    private data class Literal(
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

    /**
     * A collection of factories that will be concatenated into a single string when calling
     * [FactoryCollection.build].  Useful if you need to concatenate a string but don't
     * have access to [Context] just yet.
     */
    private class FactoryCollection(
        vararg val factories: StringFactory
    ): StringFactory

    fun build(context: Context): String = when (this) {
        is Literal -> literalValue
        is StringResource -> context.getString(resId)
        is FormattedStringResource -> context.getString(resId, *formatArgs)
        is QuantityStringResource -> context.resources.getQuantityString(
            resId,
            quantity,
            *formatArgs,
        )
        is FactoryCollection -> buildString {
            factories.forEach {
                append(it.build(context))
            }
        }
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

        fun literal(
            literalValue: String
        ): StringFactory = Literal(literalValue)

        fun collection(
            vararg factories: StringFactory
        ): StringFactory = FactoryCollection(*factories)
    }
}
