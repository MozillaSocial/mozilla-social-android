package org.mozilla.social.common.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle

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
    val textBeforeSpaceOrEnd =
        if (firstSpaceIndexAfterCursor != -1) {
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

fun TextFieldValue.replaceSymbolText(
    symbol: Char,
    newText: String,
): TextFieldValue {
    if (!text.contains(symbol)) return this
    val firstSpaceIndexAfterCursor = text.indexOf(' ', startIndex = selection.end)
    val textBeforeSpaceOrEnd =
        if (firstSpaceIndexAfterCursor != -1) {
            text.substring(0, firstSpaceIndexAfterCursor)
        } else {
            text
        }
    if (!textBeforeSpaceOrEnd.contains(symbol) ||
        textBeforeSpaceOrEnd.substringAfterLast(symbol).contains(" ")
    ) {
        return this
    }
    val rangeStart = textBeforeSpaceOrEnd.lastIndexOf(symbol) + 1
    val rangeEnd =
        if (firstSpaceIndexAfterCursor != -1) {
            firstSpaceIndexAfterCursor
        } else {
            text.length - 1
        }
    val finalText =
        text.replaceRange(
            rangeStart..rangeEnd,
            "$newText ",
        )
    return copy(
        text = finalText,
        selection = TextRange(rangeStart + newText.length + 1),
    )
}

fun buildAnnotatedStringForAccountsAndHashtags(
    baseText: String,
    style: SpanStyle,
): AnnotatedString = buildAnnotatedStringWithSymbols(listOf('@', '#'), baseText, style)

fun buildAnnotatedStringWithSymbols(
    symbols: List<Char>,
    text: String,
    style: SpanStyle,
): AnnotatedString {
    return buildAnnotatedString {
        var searchingForSymbol = true // if false then we are searching for a space
        text.forEachIndexed { index, character ->
            if (searchingForSymbol) {
                if ((index == 0 || text[index - 1] == ' ') && symbols.contains(character)) {
                    withStyle(style) {
                        append(character)
                    }
                    searchingForSymbol = false
                } else {
                    append(character)
                }
            } else {
                withStyle(style) {
                    append(character)
                }
                if (character == ' ') {
                    searchingForSymbol = true
                }
            }
        }
    }
}
