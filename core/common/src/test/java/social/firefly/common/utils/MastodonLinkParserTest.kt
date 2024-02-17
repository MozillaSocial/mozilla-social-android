package social.firefly.common.utils

import social.firefly.common.getMaxIdValue
import social.firefly.common.getSinceIdValue
import social.firefly.common.parseMastodonLinkHeader
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MaxLineLength")
class MastodonLinkParserTest {
    @Test
    fun `Since ID parsing works when since_id is the last query param`() {
        val linkHeader = "<https://mozilla.social/following?limit=40&since_id=215>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.getSinceIdValue()

        assertEquals(sinceIdValue, "215")
    }

    @Test
    fun `Since ID parsing works when since_id is not the last query param`() {
        val linkHeader = "<https://mozilla.social/following?since_id=215&limit=40>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.getSinceIdValue()

        assertEquals(sinceIdValue, "215")
    }

    @Test
    fun `Since ID parsing works when there is next and previous values`() {
        val linkHeader = "<https://mozilla.social/following?limit=40&max_id=46274>; rel=\"next\", <https://mozilla.social/following?limit=40&since_id=60171>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.getSinceIdValue()

        assertEquals(sinceIdValue, "60171")
    }

    @Test
    fun `Max ID parsing works`() {
        val linkHeader = "<https://mozilla.social/following?limit=40&max_id=46274>; rel=\"next\", <https://mozilla.social/following?limit=40&since_id=60171>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val maxIdValue = pagingLink.getMaxIdValue()

        assertEquals(maxIdValue, "46274")
    }
}
