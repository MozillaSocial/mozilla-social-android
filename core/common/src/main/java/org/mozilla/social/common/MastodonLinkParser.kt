package org.mozilla.social.common

/**
 * Parses the links header from a mastodon paging response
 * https://docs.joinmastodon.org/api/guidelines/#pagination
 */
fun String.parseMastodonLinkHeader(): List<MastodonPagingLink> {
    val separated = split(", ")
    return separated.map { link ->
        MastodonPagingLink(
            link = link.substringAfter("<").substringBefore(">"),
            rel = when(link.substringAfter("; rel=\"").substringBefore("\"")) {
                "prev" -> Rel.PREV
                else -> Rel.NEXT
            }
        )
    }
}

data class MastodonPagingLink(
    val link: String,
    val rel: Rel,
)

enum class Rel {
    PREV,
    NEXT,
}

fun List<MastodonPagingLink>.getSinceIdValue(): String? =
    find { it.rel == Rel.PREV }?.getSinceIdValue()

fun List<MastodonPagingLink>.getMaxIdValue(): String? =
    find { it.rel == Rel.NEXT }?.getMaxIdValue()

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