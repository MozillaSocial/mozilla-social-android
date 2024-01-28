package org.mozilla.social.common.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import kotlin.math.min

/**
 * @return the account string a user is typing if they are typing one, otherwise null
 */
fun TextFieldValue.accountText(): String? = findSymbolTextAtCursor('@')

/**
 * @return the hashtag string a user is typing if they are typing one, otherwise null
 */
fun TextFieldValue.hashtagText(): String? = findSymbolTextAtCursor('#')

/**
 * @return the string between a symbol and the cursor a user is typing if they are typing one, otherwise null
 */
fun TextFieldValue.findSymbolTextAtCursor(symbol: Char): String? {
    if (!text.contains(symbol) || selection.start != selection.end) return null

    val firstSpaceOrNewLineIndexAfterCursor = getFirstSpaceOrNewLineIndexAfterCursor(
        text = text,
        cursorIndex = selection.end,
    )
    val textBeforeSpaceOrEnd =
        if (firstSpaceOrNewLineIndexAfterCursor != -1) {
            text.substring(0, firstSpaceOrNewLineIndexAfterCursor)
        } else {
            text
        }
    if (!textBeforeSpaceOrEnd.contains(symbol)) return null
    val textBetweenSymbolAndCursor = textBeforeSpaceOrEnd.substringAfterLast(symbol)
    return if (!textBetweenSymbolAndCursor.contains(' ') && !textBetweenSymbolAndCursor.contains('\n')) {
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
    val firstSpaceOrNewLineIndexAfterCursor = getFirstSpaceOrNewLineIndexAfterCursor(
        text = text,
        cursorIndex = selection.end,
    )
    val textBeforeSpaceOrEnd =
        if (firstSpaceOrNewLineIndexAfterCursor != -1) {
            text.substring(0, firstSpaceOrNewLineIndexAfterCursor)
        } else {
            text
        }
    if (!textBeforeSpaceOrEnd.contains(symbol) ||
        textBeforeSpaceOrEnd.substringAfterLast(symbol).contains(' ') ||
        textBeforeSpaceOrEnd.substringAfterLast(symbol).contains('\n')
    ) {
        return this
    }
    val rangeStart = textBeforeSpaceOrEnd.lastIndexOf(symbol) + 1
    val rangeEnd =
        if (firstSpaceOrNewLineIndexAfterCursor != -1) {
            firstSpaceOrNewLineIndexAfterCursor
        } else {
            text.length - 1
        }
    // if there is a newline at the end, make sure to add it back in
    val finalChar = if (text[rangeEnd] == '\n') {
        '\n'
    } else {
        ""
    }
    val finalText =
        text.replaceRange(
            rangeStart..rangeEnd,
            "$newText $finalChar",
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
                val isStartOfWord = index == 0 || text[index - 1] == ' ' || text[index - 1] == '\n'
                if (isStartOfWord && symbols.contains(character)) {
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
                if (character == ' ' || character == '\n') {
                    searchingForSymbol = true
                }
            }
        }
    }
}

private fun getFirstSpaceOrNewLineIndexAfterCursor(
    text: String,
    cursorIndex: Int,
): Int {
    val firstSpaceIndexAfterCursor = text.indexOf(' ', startIndex = cursorIndex)
    val firstNewLineIndexAfterCursor = text.indexOf('\n', startIndex = cursorIndex)

    return when {
        firstSpaceIndexAfterCursor == -1 -> firstNewLineIndexAfterCursor
        firstNewLineIndexAfterCursor == -1 -> firstSpaceIndexAfterCursor
        else -> min(
            firstSpaceIndexAfterCursor,
            firstNewLineIndexAfterCursor
        )
    }
}
