package org.mozilla.social.common.utils

import androidx.compose.ui.text.TextRange
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
    val firstSpaceIndexAfterCursor = text.indexOf(' ', startIndex = selection.end)
    val textBeforeSpaceOrEnd = if (firstSpaceIndexAfterCursor != -1) {
        text.substring(0, firstSpaceIndexAfterCursor)
    } else {
        text
    }
    if (!textBeforeSpaceOrEnd.contains(symbol)) return null
    val textBetweenSymbolAndCursor = textBeforeSpaceOrEnd.substringAfterLast(symbol)
    return if (!textBetweenSymbolAndCursor.contains(" ")) {
        textBetweenSymbolAndCursor
    } else {
        null
    }
}

fun TextFieldValue.replaceAccount(newText: String): TextFieldValue = replaceSymbolText('@', newText)

fun TextFieldValue.replaceHashtag(newText: String): TextFieldValue = replaceSymbolText('#', newText)

fun TextFieldValue.replaceSymbolText(symbol: Char, newText: String): TextFieldValue {
    if (!text.contains(symbol)) return this
    val firstSpaceIndexAfterCursor = text.indexOf(' ', startIndex = selection.end)
    val textBeforeSpaceOrEnd = if (firstSpaceIndexAfterCursor != -1) {
        text.substring(0, firstSpaceIndexAfterCursor)
    } else {
        text
    }
    val rangeStart = textBeforeSpaceOrEnd.lastIndexOf(symbol) + 1
    val rangeEnd = if (firstSpaceIndexAfterCursor != -1) {
        firstSpaceIndexAfterCursor
    } else {
        text.length - 1
    }
    val finalText = text.replaceRange(
        rangeStart..rangeEnd,
        "$newText ",
    )
    return copy(
        text = finalText,
        selection = TextRange(rangeStart + newText.length + 1)
    )
}