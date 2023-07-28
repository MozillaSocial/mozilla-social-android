package org.mozilla.social.common.utils

import androidx.compose.ui.text.input.TextFieldValue

/**
 * @return the account string a user is typing if they are typing one, otherwise null
 */
fun TextFieldValue.accountText(): String? = findSymbolText('@')

/**
 * @return the hashtag string a user is typing if they are typing one, otherwise null
 */
fun TextFieldValue.hashtagText(): String? = findSymbolText('#')

/**
 * @return the string between a symbol and the cursor a user is typing if they are typing one, otherwise null
 */
fun TextFieldValue.findSymbolText(symbol: Char): String? {
    if (!text.contains(symbol) || selection.start != selection.end) return null
    val textBeforeCursor = text.substring(0, selection.end)
    val textBetweenSymbolAndCursor = textBeforeCursor.substringAfterLast(symbol)
    return if (!textBetweenSymbolAndCursor.contains(" ")) {
        textBetweenSymbolAndCursor
    } else {
        null
    }
}