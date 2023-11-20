package org.mozilla.social.common

import android.net.Uri

/**
 * Parses the links header from a mastodon paging response
 * [More info here](<https://docs.joinmastodon.org/api/guidelines/#pagination>)
 */
fun String.parseMastodonLinkHeader(): List<MastodonPagingLink> {
    val separated = split(", ")
    return separated.map { link ->
        MastodonPagingLink(
            link = link.substringAfter("<").substringBefore(">"),
            rel =
                when (link.substringAfter("; rel=\"").substringBefore("\"")) {
                    "prev" -> Rel.PREV
                    else -> Rel.NEXT
                },
        )
    }
}

/**
 * Mastodon paging link from a paginated response
 *
 * @property link to follow in order to go the desired direction
 * @property rel either [Rel.PREV] or [Rel.NEXT]
 * @constructor Create empty Mastodon paging link
 */
data class MastodonPagingLink(
    val link: String,
    val rel: Rel,
)

/**
 * Represents a link relation. Following the next link should show you older results. Following the
 * prev link should show you newer results.
 *
 * @constructor Create empty Rel
 */
enum class Rel {
    PREV,
    NEXT,
}

fun List<MastodonPagingLink>.getSinceIdValue(): String? = find { it.rel == Rel.PREV }?.getSinceIdValue()

fun List<MastodonPagingLink>.getMaxIdValue(): String? = find { it.rel == Rel.NEXT }?.getMaxIdValue()

private fun MastodonPagingLink.getSinceIdValue(): String =
    link
        .substringAfter("since_id=")
        .split(">")
        .first()
        .split("&")
        .first()

private fun MastodonPagingLink.getMaxIdValue(): String =
    link
        .substringAfter("max_id=")
        .split(">")
        .first()
        .split("&")
        .first()

private fun MastodonPagingLink.getLimitValue(): String {
    val uri = Uri.parse(link)
    return uri.getQueryParameter("limit")!!
}
data class HeaderLink(val maxId: String?, val sinceId: String?, val minId: String?, val limit: String?)

fun MastodonPagingLink.toHeaderLink() = HeaderLink(maxId = getMaxIdValue(), sinceId = getSinceIdValue(), minId = getMaxIdValue(), limit = getLimitValue())

fun List<MastodonPagingLink>.getNext() = firstOrNull { it.rel == Rel.NEXT }
fun List<MastodonPagingLink>.getPrev() = firstOrNull { it.rel == Rel.PREV }
