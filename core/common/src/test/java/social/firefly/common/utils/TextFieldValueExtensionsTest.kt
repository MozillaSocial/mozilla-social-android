package social.firefly.common.utils

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.test.Test
import kotlin.test.assertEquals

class TextFieldValueExtensionsTest {
    @Test
    fun findAccountNameTests() {
        var textFieldValue =
            TextFieldValue(
                text = "@test",
                selection = TextRange(3),
            )
        assertEquals("test", textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "@test",
                selection = TextRange(0),
            )
        assertEquals("test", textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "@test",
                selection = TextRange(5),
            )
        assertEquals("test", textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "@test ",
                selection = TextRange(6),
            )
        assertEquals(null, textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = " @test",
                selection = TextRange(0),
            )
        assertEquals(null, textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "foo @test",
                selection = TextRange(0),
            )
        assertEquals(null, textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "foo @test",
                selection = TextRange(3),
            )
        assertEquals(null, textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "foo @test",
                selection = TextRange(4),
            )
        assertEquals("test", textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "@test bar",
                selection = TextRange(6),
            )
        assertEquals(null, textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "@test\n" +
                        "\n" +
                        "lala",
                selection = TextRange(3),
            )
        assertEquals("test", textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "@test \n" +
                        "\n" +
                        "lala",
                selection = TextRange(2),
            )
        assertEquals("test", textFieldValue.findAccountAtCursor())

        textFieldValue =
            TextFieldValue(
                text = "lala\n" +
                        "\n" +
                        "@test",
                selection = TextRange(9),
            )
        assertEquals("test", textFieldValue.findAccountAtCursor())
    }

    @Test
    fun replaceAccountTests() {
        var textFieldValue =
            TextFieldValue(
                text = "@test",
                selection = TextRange(0),
            ).replaceAccount("new")
        assertEquals("@new ", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "@test",
                selection = TextRange(2),
            ).replaceAccount("new")
        assertEquals("@new ", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "@test",
                selection = TextRange(5),
            ).replaceAccount("new")
        assertEquals("@new ", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "@test ",
                selection = TextRange(6),
            ).replaceAccount("new")
        assertEquals("@test ", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = " @test",
                selection = TextRange(3),
            ).replaceAccount("new")
        assertEquals(" @new ", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = " @test",
                selection = TextRange(0),
            ).replaceAccount("new")
        assertEquals(" @test", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "foo @test",
                selection = TextRange(3),
            ).replaceAccount("new")
        assertEquals("foo @test", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "foo @test",
                selection = TextRange(4),
            ).replaceAccount("new")
        assertEquals("foo @new ", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "foo @test bar",
                selection = TextRange(9),
            ).replaceAccount("new")
        assertEquals("foo @new bar", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "foo @test bar",
                selection = TextRange(10),
            ).replaceAccount("new")
        assertEquals("foo @test bar", textFieldValue.text)

        textFieldValue =
            TextFieldValue(
                text = "foo\n" +
                        "@test\n" +
                        "bar",
                selection = TextRange(9),
            ).replaceAccount("new")
        assertEquals(
            "foo\n" +
                    "@new \n" +
                    "bar",
            textFieldValue.text
        )

        textFieldValue =
            TextFieldValue(
                text = "foo\n" +
                        "@test\n" +
                        "bar",
                selection = TextRange(10),
            ).replaceAccount("new")
        assertEquals(
            "foo\n" +
                    "@test\n" +
                    "bar",
            textFieldValue.text
        )
    }
}
