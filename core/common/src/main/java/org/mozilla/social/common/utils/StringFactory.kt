package org.mozilla.social.common.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * String wrapper class that supports literals and string resources
 */
sealed interface StringFactory {
    private data class Literal(val literalValue: String) : StringFactory

    private data class Resource(@param:StringRes val resId: Int) : StringFactory
    private class FormattedResource(
        @param:StringRes val resId: Int,
        vararg val formatArgs: Any
    ) : StringFactory

    fun build(context: Context): String = when (this) {
        is Literal -> literalValue
        is Resource -> context.getString(resId)
        is FormattedResource -> context.getString(resId, formatArgs)
    }

    companion object {
        fun resource(@StringRes resId: Int): StringFactory = Resource(resId)
        fun resource(
            @StringRes resId: Int,
            vararg formatArgs: Any
        ): StringFactory = FormattedResource(resId, formatArgs)

        fun literal(literalValue: String): StringFactory = Literal(literalValue)
    }
}