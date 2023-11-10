package org.mozilla.social.core.network.mastodon.model.paging

/**
 * An item that can be paginated using its id.
 */
interface NetworkPageable {
    val id: String
}
