package org.mozilla.social.common.utils

import org.mozilla.social.common.Rel
import org.mozilla.social.common.getMaxIdValue
import org.mozilla.social.common.getSinceIdValue
import org.mozilla.social.common.parseMastodonLinkHeader
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MaxLineLength")
class MastodonLinkParserTest {

    @Test
    fun sinceIdTest() {
        val linkHeader = "<https://mozilla.social/following?limit=40&since_id=215>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.getSinceIdValue()

        assertEquals(sinceIdValue, "215")
    }

    @Test
    fun sinceIdTest2() {
        val linkHeader = "<https://mozilla.social/following?since_id=215&limit=40>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.getSinceIdValue()

        assertEquals(sinceIdValue, "215")
    }

    @Test
    fun sinceIdTest3() {
        val linkHeader = "<https://mozilla.social/following?limit=40&max_id=46274>; rel=\"next\", <https://mozilla.social/following?limit=40&since_id=60171>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.getSinceIdValue()

        assertEquals(sinceIdValue, "60171")
    }

    @Test
    fun maxIdTest1() {
        val linkHeader = "<https://mozilla.social/following?limit=40&max_id=46274>; rel=\"next\", <https://mozilla.social/following?limit=40&since_id=60171>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val maxIdValue = pagingLink.getMaxIdValue()

        assertEquals(maxIdValue, "46274")
    }
}