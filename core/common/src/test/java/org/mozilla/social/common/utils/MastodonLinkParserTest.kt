package org.mozilla.social.common.utils

import org.mozilla.social.common.getSinceIdValue
import org.mozilla.social.common.parseMastodonLinkHeader
import kotlin.test.Test
import kotlin.test.assertEquals

class MastodonLinkParserTest {

    @Test
    fun sinceIdTest() {
        val linkHeader = "<https://mozilla.social/following?limit=40&since_id=215>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.first().getSinceIdValue()

        assertEquals(sinceIdValue, "215")
    }

    @Test
    fun sinceIdTest2() {
        val linkHeader = "<https://mozilla.social/following?since_id=215&limit=40>; rel=\"prev\""
        val pagingLink = linkHeader.parseMastodonLinkHeader()
        val sinceIdValue = pagingLink.first().getSinceIdValue()

        assertEquals(sinceIdValue, "215")
    }
}