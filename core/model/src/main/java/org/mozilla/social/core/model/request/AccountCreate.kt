package org.mozilla.social.core.model.request

import org.mozilla.social.core.model.Account

/**
 * Object to create a new [Account].
 */
data class AccountCreate(

    /**
     * The desired username for the account.
     */
    val username: String,

    /**
     * The email address to be used for login.
     */
    val email: String,

    /**
     * The password to be used for login.
     */
    val password: String,

    /**
     * Whether the user agrees to the local rules, terms, and policies.
     *
     * These should be presented to the user in order to allow them
     * to consent before setting this parameter to true.
     */
    val agreement: String,

    /**
     * The language of the confirmation email that will be sent.
     */
    val locale: String,

    /**
     * Text that will be reviewed by moderators if registrations require manual approval.
     */
    val reason: String? = null
)
