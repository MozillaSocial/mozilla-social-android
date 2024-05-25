package social.firefly.core.network.mastodon.model.request

import social.firefly.core.network.mastodon.model.responseBody.NetworkToken

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
