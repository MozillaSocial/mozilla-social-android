package org.mozilla.social.core.network.model

import kotlinx.datetime.Instant

/**
 * Admin-level information about a given account.
 */
data class NetworkAdminAccount(

    val accountId: String,

    /**
     * The username of the account, not including the domain.
     */
    val username: String,

    /**
     * The domain of the account.
     */
    val domain: String,

    /**
     * Date at which the account was created.
     */
    val createdAt: Instant,

    /**
     * The email address associated with the account.
     */
    val email: String,

    /**
     * The IP address last used to login to this account.
     */
    val ip: String,

    /**
     * The locale of the account.
     */
    val locale: String,

    /**
     * Text of the invite request sent to join the server.
     */
    val inviteRequest: String,

    /**
     * The current role of the account.
     */
    val role: NetworkAccountRole,

    /**
     * Whether the account has confirmed their email address.
     */
    val isConfirmed: Boolean,

    /**
     * Whether the account is currently approved.
     */
    val isApproved: Boolean,

    /**
     * Whether the account is currently disabled.
     */
    val isDisabled: Boolean,

    /**
     * Whether the account is currently silenced.
     */
    val isSilenced: Boolean,

    /**
     * Whether the account is currently suspended.
     */
    val isSuspended: Boolean,

    /**
     * User-level information about the account.
     */
    val account: NetworkAccount,

    /**
     * The ID of the application that created this account.
     */
    val createdByApplicationId: String? = null,

    /**
     * The ID of the account that invited this user.
     */
    val invitedByAccountId: String? = null
)
