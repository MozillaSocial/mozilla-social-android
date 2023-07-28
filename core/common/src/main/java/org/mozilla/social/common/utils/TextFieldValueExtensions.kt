package org.mozilla.social.common.utils

import androidx.compose.ui.graphics.Color
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
    if (!textBeforeSpaceOrEnd.contains(symbol)
        || textBeforeSpaceOrEnd.substringAfterLast(symbol).contains(" ")) return this
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

fun buildAnnotatedStringForAccountsAndHashtags(baseText: String): AnnotatedString =
    buildAnnotatedStringWithSymbols(listOf('@', '#'), baseText)

fun buildAnnotatedStringWithSymbols(symbols: List<Char>, text: String): AnnotatedString {
    //TODO get material color
    val style = SpanStyle(color = Color.Blue)
    return buildAnnotatedString {
        var searchingForSymbol = true // if false then we are searching for a space
        var start = -1
        var end = -1
        text.forEachIndexed { index, character ->
            if (searchingForSymbol) {
                if ((index == 0 || text[index - 1] == ' ') && symbols.contains(character)) {
                    start = index
                    searchingForSymbol = false
                } else {
                    append(character)
                }
            } else {
                if (character == ' ') {
                    end = index
                    searchingForSymbol = true
                }
            }

            if (start != -1 && end != -1) {
                withStyle(style) {
                    append(text.substring(start, end))
                    append(character)
                }
                start = -1
                end = -1
            }
        }
        if (start != -1) {
            withStyle(style) {
                append(text.substring(start, text.length))
            }
        }
    }
}

fun TextFieldValue.getAccountTextRanges(spanStyle: SpanStyle): List<AnnotatedString.Range<SpanStyle>> =
    getSymbolTextRanges('@', spanStyle)

fun TextFieldValue.getHashtagTextRanges(spanStyle: SpanStyle): List<AnnotatedString.Range<SpanStyle>> =
    getSymbolTextRanges('#', spanStyle)

fun TextFieldValue.getSymbolTextRanges(symbol: Char, spanStyle: SpanStyle): List<AnnotatedString.Range<SpanStyle>> {
    val ranges = mutableListOf<AnnotatedString.Range<SpanStyle>>()

    var searchingForSymbol = true // if false then we are searching for a space
    var start = -1
    var end = -1
    text.forEachIndexed { index, character ->
        if (searchingForSymbol) {
            if ((index == 0 || text[index - 1] == ' ') && character == symbol) {
                start = index
                searchingForSymbol = false
            }
        } else {
            if (character == ' ') {
                end = index
                searchingForSymbol = true
            }
        }

        if (start != -1 && end != -1) {
            start = -1
            end = -1
            ranges.add(AnnotatedString.Range(spanStyle, start, end))
        }
    }
    if (start != -1) {
        ranges.add(AnnotatedString.Range(spanStyle, start, text.length))
    }

    return ranges
}