package social.firefly.common

import android.net.Uri

/**
 * Parses the links header from a mastodon paging response
 * [More info here](<https://docs.joinmastodon.org/api/guidelines/#pagination>)
 */
fun String.parseMastodonLinkHeader(): List<social.firefly.common.MastodonPagingLink> {
    val separated = split(", ")
    return separated.map { link ->
        social.firefly.common.MastodonPagingLink(
            link = link.substringAfter("<").substringBefore(">"),
            rel =
            when (link.substringAfter("; rel=\"").substringBefore("\"")) {
                "prev" -> social.firefly.common.Rel.PREV
                else -> social.firefly.common.Rel.NEXT
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
    val rel: social.firefly.common.Rel,
)

/**
 * Represents a link relation. Following the next link should show you older results. Following the
 * prev link should show you newer results.
 */
enum class Rel {
    PREV,
    NEXT,
}

fun List<social.firefly.common.MastodonPagingLink>.getSinceIdValue(): String? =
    find { it.rel == social.firefly.common.Rel.PREV }?.getSinceIdValue()

fun List<social.firefly.common.MastodonPagingLink>.getMaxIdValue(): String? = find { it.rel == social.firefly.common.Rel.NEXT }?.getMaxIdValue()

private fun social.firefly.common.MastodonPagingLink.getSinceIdValue(): String =
    link
        .substringAfter("since_id=")
        .split(">")
        .first()
        .split("&")
        .first()

private fun social.firefly.common.MastodonPagingLink.getMaxIdValue(): String =
    link
        .substringAfter("max_id=")
        .split(">")
        .first()
        .split("&")
        .first()

private fun social.firefly.common.MastodonPagingLink.getLimitValue(): Int {
    val uri = Uri.parse(link)
    return uri.getQueryParameter("limit")?.toInt()!!
}

data class HeaderLink(
    val maxId: String?,
    val sinceId: String?,
    val minId: String?,
    val limit: Int?
)
fun social.firefly.common.MastodonPagingLink.toHeaderLink() = social.firefly.common.HeaderLink(
    maxId = getMaxIdValue(),
    sinceId = getSinceIdValue(),
    minId = getMaxIdValue(),
    limit = getLimitValue()
)

fun List<social.firefly.common.MastodonPagingLink>.getNext() = firstOrNull { it.rel == social.firefly.common.Rel.NEXT }
fun List<social.firefly.common.MastodonPagingLink>.getPrev() = firstOrNull { it.rel == social.firefly.common.Rel.PREV }

