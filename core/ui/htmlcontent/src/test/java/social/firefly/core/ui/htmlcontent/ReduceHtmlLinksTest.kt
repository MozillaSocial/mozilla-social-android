package social.firefly.core.ui.htmlcontent

import social.firefly.core.ui.htmlcontent.reduceHtmlLinks
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MaxLineLength")
class ReduceHtmlLinksTest {
    @Test
    fun reduceHtmlTest() {
        val htmlContent =
            "<p>\n" +
                "This is a test\n" +
                "<a href=\"https://mozilla.social/test/stuff\" rel=\"nofollow noopener noreferrer\" translate=\"no\" target=\"_blank\">\n" +
                "<span class=\"invisible\">https://</span>\n" +
                "<span class=\"ellipsis\">mozilla.social/test</span>\n" +
                "<span class=\"invisible\">/stuff</span>\n" +
                "</a>\n" +
                "</p>"

        assertEquals(
            htmlContent.reduceHtmlLinks(),
            "<p>\n" +
                "This is a test\n" +
                "<a href=\"https://mozilla.social/test/stuff\" rel=\"nofollow noopener noreferrer\" translate=\"no\" target=\"_blank\">\n" +
                "\n" +
                "<span class=\"ellipsis\">mozilla.social/test…</span>\n" +
                "\n" +
                "</a>\n" +
                "</p>",
        )
    }
}
