package org.mozilla.social.core.network.mastodon.model.request

import org.mozilla.social.core.network.mastodon.model.NetworkAccount
import org.mozilla.social.core.network.mastodon.model.NetworkActionType

/**
 * Object to perform an action against an [NetworkAccount].
 */
data class NetworkAccountAction(

    /**
     * The type of action to be taken.
     */
    val type: NetworkActionType? = null,

    /**
     * ID of an associated report that caused this action to be taken.
     */
    val reportId: String? = null,

    /**
     * ID of a preset warning.
     */
    val warningPresetId: String? = null,

    /**
     * Additional text for clarification of why this action was taken.
     */
    val reason: String?,

    /**
     * Whether an email should be sent to the user with the above information.
     */
    val sendEmailNotification: Boolean? = null
)
