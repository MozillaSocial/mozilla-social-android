package org.mozilla.social.core.model.request

import org.mozilla.social.core.model.Token

/**
 * Object used to revoke a [Token].
 */
data class TokenRevoke(
    /**
     * Client ID, obtained during app registration
     */
    val clientId: String,
    /**
     * Client secret, obtained during app registration
     */
    val clientSecret: String,
    /**
     * The previously obtained token, to be invalidated
     */
    val token: String,
)
