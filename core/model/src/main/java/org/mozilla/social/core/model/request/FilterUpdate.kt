package org.mozilla.social.core.model.request

import org.mozilla.social.core.model.Filter
import org.mozilla.social.core.model.FilterContext

/**
 * Object used to update an existing [Filter].
 */
data class FilterUpdate(
    /**
     * Text to be filtered.
     */
    val phrase: String,
    /**
     * Contexts to filter in. At least one context must be specified.
     */
    val context: List<FilterContext>,
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
