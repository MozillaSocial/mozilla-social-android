package org.mozilla.social.core.network.model.request

import org.mozilla.social.core.network.model.NetworkFilter
import org.mozilla.social.core.network.model.NetworkFilterContext

/**
 * Object used to create a new [NetworkFilter].
 */
data class NetworkFilterCreate(

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
    val expiresInSec: Long?
)
