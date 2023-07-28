package org.mozilla.social.common.utils

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.test.Test

class SymbolUtilsTest {

    @Test
    fun findAccountNameTests() {
        var textFieldValue = TextFieldValue(
            text = "@test",
            selection = TextRange(3),
        )
        assert(textFieldValue.accountText() == "test")

        textFieldValue = TextFieldValue(
            text = "@test",
            selection = TextRange(0),
        )
        assert(textFieldValue.accountText() == "test")

        textFieldValue = TextFieldValue(
            text = "@test",
            selection = TextRange(5),
        )
        assert(textFieldValue.accountText() == "test")

        textFieldValue = TextFieldValue(
            text = "@test ",
            selection = TextRange(6),
        )
        assert(textFieldValue.accountText() == null)

        textFieldValue = TextFieldValue(
            text = " @test",
            selection = TextRange(0),
        )
        assert(textFieldValue.accountText() == null)

        textFieldValue = TextFieldValue(
            text = "foo @test",
            selection = TextRange(0),
        )
        assert(textFieldValue.accountText() == null)

        textFieldValue = TextFieldValue(
            text = "foo @test",
            selection = TextRange(3),
        )
        assert(textFieldValue.accountText() == null)

        textFieldValue = TextFieldValue(
            text = "foo @test",
            selection = TextRange(4),
        )
        assert(textFieldValue.accountText() == "test")

        textFieldValue = TextFieldValue(
            text = "@test bar",
            selection = TextRange(6),
        )
        assert(textFieldValue.accountText() == null)
    }

    @Test
    fun replaceAccountTests() {
        var textFieldValue = TextFieldValue(
            text = "@test",
            selection = TextRange(0),
        ).replaceAccount("new")
        assert(textFieldValue.text == "@new ")

        textFieldValue = TextFieldValue(
            text = "@test",
            selection = TextRange(2),
        ).replaceAccount("new")
        assert(textFieldValue.text == "@new ")

        textFieldValue = TextFieldValue(
            text = "@test",
            selection = TextRange(5),
        ).replaceAccount("new")
        assert(textFieldValue.text == "@new ")

        textFieldValue = TextFieldValue(
            text = "@test ",
            selection = TextRange(6),
        ).replaceAccount("new")
        assert(textFieldValue.text == "@test ")

        textFieldValue = TextFieldValue(
            text = " @test",
            selection = TextRange(3),
        ).replaceAccount("new")
        assert(textFieldValue.text == " @new ")

        textFieldValue = TextFieldValue(
            text = " @test",
            selection = TextRange(0),
        ).replaceAccount("new")
        assert(textFieldValue.text == " @test")

        textFieldValue = TextFieldValue(
            text = "foo @test",
            selection = TextRange(3),
        ).replaceAccount("new")
        assert(textFieldValue.text == "foo @test")

        textFieldValue = TextFieldValue(
            text = "foo @test",
            selection = TextRange(4),
        ).replaceAccount("new")
        assert(textFieldValue.text == "foo @new ")

        textFieldValue = TextFieldValue(
            text = "foo @test bar",
            selection = TextRange(9),
        ).replaceAccount("new")
        assert(textFieldValue.text == "foo @new bar")

        textFieldValue = TextFieldValue(
            text = "foo @test bar",
            selection = TextRange(10),
        ).replaceAccount("new")
        assert(textFieldValue.text == "foo @test bar")
    }
}