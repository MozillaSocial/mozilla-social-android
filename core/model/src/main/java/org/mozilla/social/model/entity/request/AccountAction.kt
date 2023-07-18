package org.mozilla.social.model.entity.request

import org.mozilla.social.model.entity.Account
import org.mozilla.social.model.entity.ActionType

/**
 * Object to perform an action against an [Account].
 */
data class AccountAction(

    /**
     * The type of action to be taken.
     */
    val type: ActionType? = null,

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
