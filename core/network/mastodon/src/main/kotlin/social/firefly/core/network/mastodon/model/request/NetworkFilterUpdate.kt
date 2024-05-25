package social.firefly.core.network.mastodon.model.request

import social.firefly.core.network.mastodon.model.responseBody.NetworkFilter
import social.firefly.core.network.mastodon.model.responseBody.NetworkFilterContext

/**
 * Object used to update an existing [NetworkFilter].
 */
data class NetworkFilterUpdate(
    /**
     * Text to be filtered.
     */
    val phrase: String,
    /**
     * Contexts to filter in. At least one context must be specified.
     */
    val context: List<NetworkFilterContext>,
    /**
     * Should the server irreversibly drop matching entities from home and notifications?
     */
    val isIrreversible: Boolean?,
    /**
     * Consider word boundaries?
     */
    val wholeWord: Boolean?,
    /**
     * Number of seconds from now the filter should expire.
     *
     * If null, will never expire.
     */
    val expiresInSec: Long?,
)
