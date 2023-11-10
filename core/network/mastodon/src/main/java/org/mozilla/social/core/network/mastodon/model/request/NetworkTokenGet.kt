package org.mozilla.social.core.network.mastodon.model.request

import org.mozilla.social.core.network.mastodon.model.NetworkGrantType
import org.mozilla.social.core.network.mastodon.model.NetworkToken

/**
 * Object used to get a new [NetworkToken].
 */
data class NetworkTokenGet(

    /**
     * Client ID, obtained during app registration.
     */
    val clientId: String,

    /**
     * Client secret, obtained during app registration.
     */
    val clientSecret: String,

    /**
     * URI to redirect the user to.
     *
     * If this parameter is set to `urn:ietf:wg:oauth:2.0:oob`
     * then the token will be shown instead.
     *
     * Must match one of the redirect URIs declared during app registration.
     */
    val redirectUri: String,

    /**
     * Grant type, set to [NetworkGrantType.AuthorizationCode] if code
     * is provided in order to gain user-level access.
     *
     * Otherwise, set equal to [NetworkGrantType.ClientCredentials] to
     * obtain app-level access only.
     */
    val grantType: NetworkGrantType,

    /**
     * List of requested OAuth scopes, separated by spaces.
     *
     * Must be a subset of scopes declared during app registration.
     * If not provided, defaults to read.
     */
    val scope: String?,

    /**
     * A user authorization code, obtained via /oauth/authorize.
     */
    val code: String?
)
