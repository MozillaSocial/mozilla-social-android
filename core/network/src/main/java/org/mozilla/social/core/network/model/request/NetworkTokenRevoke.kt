package org.mozilla.social.core.network.model.request

import org.mozilla.social.core.network.model.NetworkToken

/**
 * Object used to revoke a [NetworkToken].
 */
data class NetworkTokenRevoke(

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
